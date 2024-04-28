package com.xue.bean;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 车辆现状
 *
 * @author Xue
 * @create 2024-04-28 10:14
 */
@Data
@EqualsAndHashCode(callSuper=false)
public class VehicleStatus extends Agreement {

    private Double longitude;

    private Double latitude;

    private Integer height;

    private Integer speed;

    private Integer direction;

}
