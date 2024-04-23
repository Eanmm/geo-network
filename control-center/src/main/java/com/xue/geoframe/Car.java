package com.xue.geoframe;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Xue
 * @create 2024-04-23 13:34
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Car {

    /**
     * 车辆id标识
     */
    private Integer id;

    /**
     * 经度
     */
    private Double longitude;

    /**
     * 维度
     */
    private Double latitude;

    public Car(SimpleCam simpleCam) {
        this.id = simpleCam.stationId;
        this.longitude = simpleCam.longitude / 1e7;
        this.latitude = simpleCam.longitude / 1e7;
    }

}
