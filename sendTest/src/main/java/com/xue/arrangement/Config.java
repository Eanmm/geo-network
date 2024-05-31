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

    private Integer stationId = 101;

    private Integer length = 40;

    private Integer width = 20;

    private String mac = "00:0e:8E:52:A8:33";

    private Integer localPortForUdpLinkLayer = 4000;
    private String remoteAddressForUdpLinkLayer = "127.0.0.1:4001";

    private Double longitude = 119.902185;

    private Double latitude = 30.267588;

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
            } else if ("--longitude".equals(argItem)) {
                i++;
                this.longitude = Double.parseDouble(args[i]);
            } else if ("--latitude".equals(argItem)) {
                i++;
                this.latitude = Double.parseDouble(args[i]);
            }
        }
        if (stationId == null) {
            throw new RuntimeException("请配置stationId!");
        }
        log.info("配置信息读取完成:" +
                "\n车辆Id:{}" +
                "\nlength:{}" +
                "\nwidth:{}" +
                "\nmac地址:{}" +
                "\nlongitude:{}" +
                "\nlatitude:{}" +
                "\nudp2eth server port:{}" +
                "\nudp2eth client:{}", stationId, length, width, mac, longitude, latitude, localPortForUdpLinkLayer, remoteAddressForUdpLinkLayer
        );
    }

}
