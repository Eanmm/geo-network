package com.xue.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author Xue
 * @create 2024-04-22 14:26
 */
@Data
@Component
@ConfigurationProperties("router")
public class RouterConfig {

    private Integer countryCode;

    private Integer localPortForUdpLinkLayer;

    private String remoteAddressForUdpLinkLayer;

    private String macAddress;

    private Integer stationId;

}
