package com.xue.frame;

import com.xue.arrangement.Config;
import com.xue.bean.AlertRelay;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Xue
 * @create 2024-04-28 10:23
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Warning {

    private Integer stationId;

    private Double longitude;

    private Double latitude;

    private Integer type;

    private Integer length;

    private Integer width;

    public Warning(Car car) {
        this.stationId = car.getStationId();
        this.longitude = car.getLongitude();
        this.latitude = car.getLatitude();
        this.type = 0;
        this.length = car.getLength() + 2;
        this.width = 0;
    }

    public Warning(AlertRelay alertRelay) {
        this.stationId = Config.getInstance().getStationId();
        this.longitude = alertRelay.getLongitude();
        this.latitude = alertRelay.getLatitude();
        this.type = alertRelay.getAlarmType();
        this.length = alertRelay.getLength();
        this.width = alertRelay.getWidth();
    }

    public Warning(SimpleDenm simpleDenm, Integer type) {
        this.stationId = simpleDenm.stationId;
        this.longitude = simpleDenm.longitude / 1e7;
        this.latitude = simpleDenm.latitude / 1e7;
        this.type = type;
        this.length = simpleDenm.semiMajorConfidence * 100;
        this.width = simpleDenm.semiMajorConfidence * 100;
    }

}
