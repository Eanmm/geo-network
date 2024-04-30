package com.xue.frame;

import com.xue.cache.Region;
import com.xue.arrangement.Config;
import com.xue.bean.AlertRelay;
import com.xue.communication.QtMutual;
import lombok.extern.slf4j.Slf4j;
import net.gcdc.asn1.uper.UperEncoder;
import net.gcdc.camdenm.CoopIts;
import net.gcdc.geonetworking.*;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author Xue
 * @create 2024-04-28 10:26
 */
@Slf4j
public class GeoFrame {

    private GeoFrame() {
    }

    private static class GeoFrameInstance {
        private static final GeoFrame INSTANCE = new GeoFrame();
    }

    public static GeoFrame getInstance() {
        return GeoFrameInstance.INSTANCE;
    }

    private final Config config = Config.getInstance();

    private ExecutorService executor;
    private VehiclePositionProvider vehiclePositionProvider;
    private GeonetStation station;
    private Thread stationThread;
    private BtpSocket btpSocket;
    private StatsLogger statsLogger;

    /**
     * 初始化
     */
    public void initialize() throws SocketException {
        log.info("geo frame start");
        executor = Executors.newCachedThreadPool();
        MacAddress senderMac = new MacAddress(config.getMac());
        Address address =
                new Address(
                        true, // isManual,
                        StationType.values()[12], // 5 for passenger car
                        86,
                        senderMac.value());
        vehiclePositionProvider = new VehiclePositionProvider(address);

        InetSocketAddress remoteAddressForUdpLinkLayer =
                new SocketAddressFromString(config.getRemoteAddressForUdpLinkLayer())
                        .asInetSocketAddress();
        LinkLayer linkLayer =
                new LinkLayerUdpToEthernet(config.getLocalPortForUdpLinkLayer(), remoteAddressForUdpLinkLayer, true);


        station = new GeonetStation(new StationConfig(), linkLayer, vehiclePositionProvider, senderMac);
        station.regionalJudgmentIsRequired.set(false);

        stationThread = new Thread(station);
        stationThread.start();

        station.startBecon();

        btpSocket = BtpSocket.on(station);

        statsLogger = new StatsLogger(executor);

        for (int i = 0; i < 2; i++) {
            executor.submit(sendToVehicle);
        }
    }

    private final Runnable sendToVehicle =
            new Runnable() {
                @Override
                public void run() {
                    log.info("BtpSocket thread starting...");
                    try {
                        while (true) {
                            BtpPacketWithArea btpPacketWithArea = btpSocket.receiveWithArea();
                            BtpPacket btpPacket = btpPacketWithArea.getBtpPacket();
                            byte[] payload = btpPacket.payload();
                            int destinationPort = btpPacket.destinationPort();
                            simpleFromProper(payload, destinationPort, btpPacketWithArea.getArea(), btpPacketWithArea.getInside());
                        }
                    } catch (InterruptedException e) {
                        log.warn("BTP socket interrupted during receive");
                    }
                    log.info("BtpSocket thread closing!");
                }
            };


    /* BTP ports for CAM/DENM/iCLCM/CUSTOM */
    private static final short PORT_CAM = 2001;
    private static final short PORT_DENM = 2002;
    /* Message lifetime */
    private static final double CAM_LIFETIME_SECONDS = 0.9;

    private void simpleFromProper(
            byte[] payload, int destinationPort, Area area, Boolean inside) {
        switch (destinationPort) {
            case PORT_CAM:
                statsLogger.incRxCam();
                CoopIts.Cam cam = UperEncoder.decode(payload, CoopIts.Cam.class);
                SimpleCam simpleCam = new SimpleCam(cam);
                Car car = new Car(simpleCam);
                Region.getInstance().fetchCar(car);

                break;
            case PORT_DENM:
                statsLogger.incRxDenm();
                CoopIts.Denm denm = UperEncoder.decode(payload, CoopIts.Denm.class);
                SimpleDenm simpleDenm = new SimpleDenm(denm);
                if (inside) {
                    // 立刻透传
                    QtMutual.sendMsg(new AlertRelay(simpleDenm.longitude / 1e7,
                            simpleDenm.latitude / 1e7,
                            area.type().code(),
                            simpleDenm.semiMajorConfidence * 100,
                            simpleDenm.semiMinorConfidence * 100
                    ));
                }
                Warning warning = new Warning(simpleDenm, area.type().code());
                Region.getInstance().fetchWarning(warning);
                // 整合告警信息
                //vehicleWarning.check(simpleDenm, area);
                break;
            default:
                // fallthrough
        }
    }


    /**
     * Broadcast a proper CAM message.
     *
     * @param cam A proper CAM message.
     */
    private void send(CoopIts.Cam cam) {
        byte[] bytes;
        try {
            bytes = UperEncoder.encode(cam);
        } catch (IllegalArgumentException | UnsupportedOperationException e) {
            log.warn("Failed to encode CAM {}, ignoring", cam, e);
            return;
        }
        BtpPacket packet = BtpPacket.singleHop(bytes, PORT_CAM, CAM_LIFETIME_SECONDS);
        try {
            btpSocket.send(packet);
        } catch (IOException e) {
            log.warn("failed to send cam", e);
        }
    }

    /**
     * Broadcast a proper DENM message to the specified GeoBroadcast destination.
     *
     * @param denm        A proper DENM message.
     * @param destination The geographical destination of the message.
     */
    private void send(CoopIts.Denm denm, Destination.Geobroadcast destination) {
        byte[] bytes;
        try {
            bytes = UperEncoder.encode(denm);
        } catch (IllegalArgumentException | UnsupportedOperationException e) {
            log.error("Failed to encode DENM {}, ignoring", denm, e);
            return;
        }
        BtpPacket packet = BtpPacket.customDestination(bytes, PORT_DENM, destination);
        try {
            btpSocket.send(packet);
        } catch (IOException e) {
            log.warn("failed to send denm", e);
        }
    }


    public void sendCam(SimpleCam simpleCam) {
        CoopIts.Cam cam = simpleCam.asCam();
        // 更新位置信息
        double latitude = (double) simpleCam.getLatitude();
        latitude /= 1e7;
        double longitude = (double) simpleCam.getLongitude();
        longitude /= 1e7;
        double speedMetersPerSecond = (double) simpleCam.speed;
        speedMetersPerSecond *= 100;
        double headingDegreesFromNorth = (double) simpleCam.heading;
        headingDegreesFromNorth *= 10;
        vehiclePositionProvider.update(
                latitude, longitude, speedMetersPerSecond, headingDegreesFromNorth);
        send(cam);
        statsLogger.incTxCam();
    }


    public void sendDenm(SimpleDenm simpleDenm, Boolean alone, Integer areaType) {
        CoopIts.Denm denm = simpleDenm.asDenm();
        Position position = alone ? updatePositionByDenm(simpleDenm) : vehiclePositionProvider.getPosition();
        int semiMajor = simpleDenm.semiMajorConfidence;
        int semiMinor = simpleDenm.semiMinorConfidence;
        Area.Type type = Area.Type.fromCode(areaType);
        Area target = type.createByAreaType(position, semiMajor, semiMinor, 0);
        send(denm, Destination.Geobroadcast.geobroadcast(target));
        statsLogger.incTxDenm();
    }


    public Position updatePositionByDenm(SimpleDenm simpleDenm) {
        double latitude = simpleDenm.latitude;
        double longitude = simpleDenm.longitude;
        latitude /= 1e7;
        longitude /= 1e7;
        vehiclePositionProvider.update(latitude, longitude, 0, 0);
        return vehiclePositionProvider.getPosition();
    }


}
