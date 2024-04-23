package com.xue.geoframe;

import com.xue.config.RouterConfig;
import lombok.extern.slf4j.Slf4j;
import net.gcdc.asn1.uper.UperEncoder;
import net.gcdc.camdenm.CoopIts;
import net.gcdc.geonetworking.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author Xue
 * @create 2024-04-22 14:24
 */
@Slf4j
@Component
public class GeoFrame {

    private final RouterConfig routerConfig;

    private final VehicleWarning vehicleWarning;

    @Autowired
    public GeoFrame(RouterConfig routerConfig, VehicleWarning vehicleWarning) {
        this.routerConfig = routerConfig;
        this.vehicleWarning = vehicleWarning;
    }

    private ExecutorService executor;
    private VehiclePositionProvider vehiclePositionProvider;
    private GeonetStation station;
    private Thread stationThread;
    private BtpSocket btpSocket;
    private StatsLogger statsLogger;

    @PostConstruct
    public void startGeo() throws SocketException {
        log.info("geoframe start");

        executor = Executors.newCachedThreadPool();
        MacAddress senderMac = new MacAddress(routerConfig.getMacAddress());
        Address address =
                new Address(
                        true, // isManual,
                        StationType.values()[12], // 5 for passenger car
                        routerConfig.getCountryCode(),
                        senderMac.value());
        vehiclePositionProvider = new VehiclePositionProvider(address);

        InetSocketAddress remoteAddressForUdpLinkLayer =
                new SocketAddressFromString(routerConfig.getRemoteAddressForUdpLinkLayer())
                        .asInetSocketAddress();
        LinkLayer linkLayer =
                new LinkLayerUdpToEthernet(routerConfig.getLocalPortForUdpLinkLayer(), remoteAddressForUdpLinkLayer, true);


        station = new GeonetStation(new StationConfig(), linkLayer, vehiclePositionProvider, senderMac);
        stationThread = new Thread(station);
        stationThread.start();

        station.startBecon();

        btpSocket = BtpSocket.on(station);

        statsLogger = new StatsLogger(executor);

        for (int i = 0; i < 2; i++) {
            executor.submit(sendToVehicle);
        }
    }


    /* BTP ports for CAM/DENM/iCLCM/CUSTOM */
    private static final short PORT_CAM = 2001;
    private static final short PORT_DENM = 2002;
    /* Message lifetime */
    private static final double CAM_LIFETIME_SECONDS = 0.9;

    /**
     * Broadcast a proper CAM message.
     *
     * @param cam A proper CAM message.
     */
    public void send(CoopIts.Cam cam) {
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
        send(cam);
        statsLogger.incTxCam();
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
    }

    public void sendDenmArea(SimpleDenm simpleDenm, Boolean alone) {
        CoopIts.Denm denm = simpleDenm.asDenm();
        Position position = alone ? updatePositionByDenm(simpleDenm) : vehiclePositionProvider.getPosition();
        int radius = simpleDenm.semiMajorConfidence;
        Area target = Area.circle(position, radius);
        send(denm, Destination.Geobroadcast.geobroadcast(target));
        statsLogger.incTxDenm();
    }

    public void sendDenmRectangle(SimpleDenm simpleDenm, Boolean alone) {
        CoopIts.Denm denm = simpleDenm.asDenm();
        Position position = alone ? updatePositionByDenm(simpleDenm) : vehiclePositionProvider.getPosition();
        int semiMajor = simpleDenm.semiMajorConfidence;
        int semiMinor = simpleDenm.semiMinorConfidence;
        Area target = Area.ellipse(position, semiMajor, semiMinor, 0);
        send(denm, Destination.Geobroadcast.geobroadcast(target));
        statsLogger.incTxDenm();
    }

    public void sendDenmEllipse(SimpleDenm simpleDenm, Boolean alone) {
        CoopIts.Denm denm = simpleDenm.asDenm();
        Position position = alone ? updatePositionByDenm(simpleDenm) : vehiclePositionProvider.getPosition();
        int semiMajor = simpleDenm.semiMajorConfidence;
        int semiMinor = simpleDenm.semiMinorConfidence;
        Area target = Area.ellipse(position, semiMajor, semiMinor, 0);
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
                            simpleFromProper(payload, destinationPort, btpPacketWithArea.getArea());
                        }
                    } catch (InterruptedException e) {
                        log.warn("BTP socket interrupted during receive");
                    }
                    log.info("BtpSocket thread closing!");
                }
            };

    private void simpleFromProper(
            byte[] payload, int destinationPort, Area area) {
        switch (destinationPort) {
            case PORT_CAM:
                statsLogger.incRxCam();
                CoopIts.Cam cam = UperEncoder.decode(payload, CoopIts.Cam.class);
                SimpleCam simpleCam = new SimpleCam(cam);
                vehicleWarning.check(simpleCam);

                break;
            case PORT_DENM:
                statsLogger.incRxDenm();
                CoopIts.Denm denm = UperEncoder.decode(payload, CoopIts.Denm.class);
                SimpleDenm simpleDenm = new SimpleDenm(denm);
                vehicleWarning.check(simpleDenm, area);
                break;
            default:
                // fallthrough
        }
    }
}
