package com.xue;

import com.xue.bean.InstantInformation;
import com.xue.cache.Region;
import com.xue.communication.QtMutual;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author Xue
 * @create 2024-04-29 15:13
 */
public class RealTimeMonitoring {

    private RealTimeMonitoring() {
    }

    private static class RealTimeMonitoringHolder {
        private static final RealTimeMonitoring INSTANCE = new RealTimeMonitoring();
    }

    public static RealTimeMonitoring getInstance() {
        return RealTimeMonitoringHolder.INSTANCE;
    }

    public void run() {
        ScheduledExecutorService pool = Executors.newSingleThreadScheduledExecutor();
        pool.scheduleAtFixedRate(() -> {
            InstantInformation instantInformation = new InstantInformation(Region.getInstance().getCars(), Region.getInstance().getWarnings());
            QtMutual.sendMsg(instantInformation);
        }, 2, 1, TimeUnit.SECONDS);
    }


}
