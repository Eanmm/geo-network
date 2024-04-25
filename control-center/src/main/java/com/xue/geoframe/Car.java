package com.xue.geoframe;

import lombok.*;

import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author Xue
 * @create 2024-04-23 13:34
 */
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Car {

    /**
     * 车辆id标识
     */
    private Integer stationId;

    /**
     * 经度
     */
    private Double longitude;

    /**
     * 维度
     */
    private Double latitude;

    private ExecutorService pool = Executors.newSingleThreadExecutor();
    /**
     * 动态移除标识
     */
    private AtomicBoolean retain = new AtomicBoolean(false);

    public Car(SimpleCam simpleCam) {
        this.stationId = simpleCam.stationId;
        this.longitude = simpleCam.longitude / 1e7;
        this.latitude = simpleCam.longitude / 1e7;
    }

    public void prolong() {
        retain.set(true);
    }

    public void automaticallyDies() {
        pool.submit(() -> {
            while (true) {
                try {
                    TimeUnit.SECONDS.sleep(2);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                if (!retain.getAndSet(false)) {
                    break;
                }
            }
            VehicleWarning.removeCarById(this.stationId);
        });
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Car car = (Car) o;
        return Objects.equals(stationId, car.stationId) && Objects.equals(longitude, car.longitude) && Objects.equals(latitude, car.latitude);
    }

    @Override
    public int hashCode() {
        return Objects.hash(stationId, longitude, latitude);
    }
}
