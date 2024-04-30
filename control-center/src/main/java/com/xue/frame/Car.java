package com.xue.frame;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Xue
 * @create 2024-04-28 10:20
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Car {


    private Integer stationId;

    private Double longitude;

    private Double latitude;

    private Integer height;

    private Integer speed;

    private Integer direction;


    private Integer length;

    private Integer width;


    public Car(SimpleCam simpleCam) {
        this.stationId = simpleCam.stationId;
        this.longitude = simpleCam.longitude / 1e7;
        this.latitude = simpleCam.latitude / 1e7;
        this.height = simpleCam.altitude;
        this.speed = simpleCam.speed;
        this.direction = simpleCam.heading;
        this.length = simpleCam.vehicleLength;
        this.width = simpleCam.vehicleWidth;
    }

}
