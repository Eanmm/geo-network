package com.xue;

import com.xue.bean.InstantInformation;
import com.xue.cache.Region;
import com.xue.communication.QtMutual;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author Xue
 * @create 2024-04-29 15:13
 */
@Slf4j
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
            // log.info("statusQuo:{}", instantInformation);
            QtMutual.sendMsg(instantInformation);
        }, 2, 1, TimeUnit.SECONDS);
    }


}
