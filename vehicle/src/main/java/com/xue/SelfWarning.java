package com.xue;

import com.xue.cache.Region;
import com.xue.frame.GeoFrame;
import com.xue.frame.MessageFactory;
import com.xue.frame.SimpleDenm;
import com.xue.frame.Warning;
import lombok.Setter;
import net.gcdc.geonetworking.Position;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author Xue
 * @create 2024-04-29 17:25
 */
@Setter
public class SelfWarning {

    private volatile Warning warning;

    private volatile Position position;


    private SelfWarning() {
        ScheduledExecutorService pool = Executors.newSingleThreadScheduledExecutor();
        pool.scheduleAtFixedRate(() -> {
            if (warning != null) {
                if (new Position(warning.getLatitude(), warning.getLongitude()).distanceInMetersTo(position) < 50) {
                    Region.getInstance().fetchWarning(warning);
                    SimpleDenm denm = MessageFactory.getInstance().getDenm(warning);
                    GeoFrame.getInstance().sendDenm(denm, true, warning.getType());
                } else {
                    warning = null;
                }
            }
        }, 0, 1, TimeUnit.SECONDS);
    }

    private static class SingletonHolder {
        private static final SelfWarning INSTANCE = new SelfWarning();
    }

    public static SelfWarning getInstance() {
        return SingletonHolder.INSTANCE;
    }
}
