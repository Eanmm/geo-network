package com.xue.utils;

import com.xue.config.RouterConfig;

/**
 * @author Xue
 * @create 2024-04-30 14:53
 */
public class ActiveConfig {
    private ActiveConfig() {
    }

    private static class ActiveConfigHolder {
        private static final ActiveConfig INSTANCE = new ActiveConfig();
    }

    public static ActiveConfig getInstance() {
        return ActiveConfigHolder.INSTANCE;
    }

    private volatile RouterConfig routerConfig;

    public Integer getStationId() {
        if (routerConfig == null) {
            synchronized (ActiveConfigHolder.class) {
                if (routerConfig == null) {
                    routerConfig = SpringUtils.getBean(RouterConfig.class);
                }
            }
        }
        return routerConfig.getStationId();
    }


}
