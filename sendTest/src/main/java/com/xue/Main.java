package com.xue;

import com.xue.arrangement.Config;
import com.xue.frame.*;

import java.net.SocketException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author Xue
 * @create 2024-05-31 16:31
 */
public class Main {
    public static void main(String[] args) throws SocketException {
        Config.getInstance().loadConfig(args);
        GeoFrame.getInstance().initialize();
        periodicSend();

    }

    private static void periodicSend() {
        Config config = Config.getInstance();
        Integer stationId = config.getStationId();
        Double longitude = config.getLongitude();
        Double latitude = config.getLatitude();
        Integer length = config.getLength();
        Integer width = config.getWidth();

        SimpleCam cam = MessageFactory.getInstance().getCam(new Car(stationId,longitude,latitude,0,0,0,1,1));
        SimpleDenm denm = MessageFactory.getInstance().getDenm(new Warning(stationId, longitude, latitude, 0, length, width));
        GeoFrame geoFrame = GeoFrame.getInstance();
        ScheduledExecutorService pool = Executors.newSingleThreadScheduledExecutor();
        pool.scheduleAtFixedRate(() -> {
            geoFrame.sendCam(cam);
            geoFrame.sendDenm(denm,true,0);
        }, 2, 1, TimeUnit.SECONDS);
    }
}
