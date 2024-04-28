package com.xue.bean;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Xue
 * @create 2024-04-28 10:17
 */
@Data
@EqualsAndHashCode(callSuper=false)
public class VehicleAlerts extends Agreement {

    private Integer alarmType;

    private Double longitude;

    private Double latitude;

}
