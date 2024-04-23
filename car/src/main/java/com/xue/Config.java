package com.xue;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

/**
 * @author xue
 * @create 2022-10-26 15:28
 */
@Slf4j
@Getter
public enum Config {
    /**
     * 单例
     */
    INSTANCE;

    /**
     * 基站IP地址
     */
    public static String address = "127.0.0.1";
    /**
     * 基站配置 portRcvFromVehicle=5000
     */
    public static Integer port = 5000;

    public static Integer localPort = 5001;

    private Integer radius = 1;

    private String host = "tcp://192.168.0.158:1884";

    private String clientId = "xiaoche1";

    private final String userName = "admin";

    private final String password = "admin123";

    private String topic = "/hguwb_to_gps/conversion_coordinates/89697";

    private Integer stationId = 201;

    private Integer gpsTcpPort = 1000;

    private Boolean baseStation = false;

    private Position position = new Position();


    public Config loadConfig(String[] args) throws IOException {
        for (int i = 0; i < args.length; i++) {
            String argItem = args[i];
            if ("--rad".equals(argItem)) {
                i++;
                this.radius = Integer.parseInt(args[i]);
            } else if ("--ip".equals(argItem)) {
                i++;
                address = args[i];
            } else if ("--port".equals(argItem)) {
                i++;
                port = Integer.parseInt(args[i]);
            } else if ("--localPort".equals(argItem)) {
                i++;
                localPort = Integer.parseInt(args[i]);
            } else if ("--host".equals(argItem)) {
                i++;
                host = args[i];
            } else if ("--clientId".equals(argItem)) {
                i++;
                clientId = args[i];
            } else if ("--topic".equals(argItem)) {
                i++;
                topic = topic.replaceFirst("89697", args[i]);
            } else if ("--stationId".equals(argItem)) {
                i++;
                stationId = Integer.parseInt(args[i]);
            } else if ("--base".equals(argItem)) {
                baseStation = true;
            } else if ("--lon".equals(argItem)){
                i++;
                position.setLongitude(Double.parseDouble(args[i]));
            } else if ("--lat".equals(argItem)){
                i++;
                position.setLatitude(Double.parseDouble(args[i]));
            }
        }
        if (radius == null) {
            throw new RuntimeException("请配置警告中心点经纬度及警告半径！");
        }
        log.info("配置信息读取完成：" +
                "\n信息发送至：{}:{}，" +
                "\n监听端口：{}，" +
                "\nmqtt的ClientId：{}，" +
                "\nmqtt的topic：{}，" +
                "\nstationId：{}，" +
                "\n警告半径：{}", address, port, localPort, clientId, topic, stationId, radius
        );
        return this;
    }

}
