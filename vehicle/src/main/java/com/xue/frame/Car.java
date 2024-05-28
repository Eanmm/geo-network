package com.xue.frame;

import com.xue.arrangement.Config;
import com.xue.bean.VehicleStatus;
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


    public Car(VehicleStatus vehicleStatus) {
        this.stationId = Config.getInstance().getStationId();
        this.longitude = vehicleStatus.getLongitude();
        this.latitude = vehicleStatus.getLatitude();
        this.height = vehicleStatus.getHeight();
        this.speed = vehicleStatus.getSpeed();
        this.direction = vehicleStatus.getDirection();
        this.length = Config.getInstance().getLength();
        this.width = Config.getInstance().getWidth();
    }

    public Car(SimpleCam simpleCam) {
        this.stationId = simpleCam.stationId;
        this.longitude = simpleCam.longitude / 1e7;
        this.latitude = simpleCam.latitude / 1e7;
        this.height = simpleCam.altitude;
        this.speed = simpleCam.speed * 100;
        this.direction = simpleCam.heading;
        this.length = simpleCam.vehicleLength;
        this.width = simpleCam.vehicleWidth;
    }

}
