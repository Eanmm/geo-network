package com.xue.frame;

import com.xue.entity.AlertEntity;
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

    public Warning(AlertEntity car) {
        this.stationId = car.getStationId();
        // this.stationId = ActiveConfig.getInstance().getStationId();
        this.longitude = car.getLongitude();
        this.latitude = car.getLatitude();
        this.type = car.getType();
        this.length = car.getLength();
        this.width = car.getWidth();
    }

    public Warning(SimpleDenm simpleDenm, Integer type) {
        this.stationId = simpleDenm.stationId;
        this.longitude = simpleDenm.longitude / 1e7;
        this.latitude = simpleDenm.latitude / 1e7;
        this.type = type;
        this.length = simpleDenm.semiMajorConfidence * 100;
        this.width = simpleDenm.semiMajorConfidence * 100;
    }

    public AlertEntity toAlertEntity() {
        AlertEntity alertEntity = new AlertEntity();
        alertEntity.setStationId(this.stationId);
        alertEntity.setLongitude(this.longitude);
        alertEntity.setLatitude(this.latitude);
        alertEntity.setType(this.type);
        alertEntity.setLength(this.length);
        alertEntity.setWidth(this.width);
        alertEntity.setShowFlag(true);
        return alertEntity;
    }

}
