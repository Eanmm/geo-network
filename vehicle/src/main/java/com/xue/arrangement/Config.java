package com.xue.arrangement;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Xue
 * @create 2024-04-28 10:47
 */
@Slf4j
@Getter
public class Config {

    private Config() {
    }

    private static class ConfigInstance {
        private static final Config INSTANCE = new Config();
    }

    public static Config getInstance() {
        return ConfigInstance.INSTANCE;
    }

    private Integer stationId = 999;

    private Integer length = 40;

    private Integer width = 20;

    private String mac = "00:0e:8E:52:A8:33";

    private Integer localPortForUdpLinkLayer = 4000;
    private String remoteAddressForUdpLinkLayer = "192.168.3.2:4001";

    public void loadConfig(String[] args) {
        for (int i = 0; i < args.length; i++) {
            String argItem = args[i];
            if ("--stationId".equals(argItem)) {
                i++;
                this.stationId = Integer.parseInt(args[i]);
            } else if ("--length".equals(argItem)) {
                i++;
                this.length = Integer.parseInt(args[i]);
            } else if ("--width".equals(argItem)) {
                i++;
                this.width = Integer.parseInt(args[i]);
            } else if ("--mac".equals(argItem)) {
                i++;
                this.mac = args[i];
            } else if ("--localPortForUdpLinkLayer".equals(argItem)) {
                i++;
                this.localPortForUdpLinkLayer = Integer.parseInt(args[i]);
            } else if ("--remoteAddressForUdpLinkLayer".equals(argItem)) {
                i++;
                this.remoteAddressForUdpLinkLayer = args[i];
            }
        }
        if (stationId == null) {
            throw new RuntimeException("请配置stationId!");
        }
        log.info("配置信息读取完成:" +
                "\n车辆Id:{}" +
                "\n车辆长度:{}" +
                "\n车辆宽度:{}" +
                "\nmac地址:{}" +
                "\nudp2eth server port:{}" +
                "\nudp2eth client:{}", stationId, length, width, mac, localPortForUdpLinkLayer, remoteAddressForUdpLinkLayer
        );
    }

}
