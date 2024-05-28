package com.xue.frame;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.xue.entity.CarEntity;
import com.xue.entity.Path;
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

    @JsonIgnore
    private Path path;


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

    public Car(CarEntity car) {
        this.stationId = car.getStationId();
        this.longitude = car.getLongitude();
        this.latitude = car.getLatitude();
        this.height = 66;
        this.speed = 0;
        this.direction = car.getDirection();
        this.length = car.getLength();
        this.width = car.getWidth();
    }

    public CarEntity carEntityUseUpdate() {
        CarEntity carEntity = new CarEntity();
        carEntity.setStationId(this.stationId);
        carEntity.setLongitude(this.longitude);
        carEntity.setLatitude(this.latitude);
        return carEntity;
    }
}
