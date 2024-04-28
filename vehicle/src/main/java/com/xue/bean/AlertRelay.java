package com.xue.bean;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Xue
 * @create 2024-04-28 10:18
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class AlertRelay extends Agreement {

    private Double longitude;

    private Double latitude;

    private Integer alarmType;

    private Integer length;

    private Integer width;

    public AlertRelay(Double longitude, Double latitude, Integer alarmType, Integer length, Integer width) {
        super(3);
        this.longitude = longitude;
        this.latitude = latitude;
        this.alarmType = alarmType;
        this.length = length;
        this.width = width;
    }
}
