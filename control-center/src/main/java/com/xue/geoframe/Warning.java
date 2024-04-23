package com.xue.geoframe;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.gcdc.geonetworking.Area;

/**
 * @author Xue
 * @create 2024-04-23 13:59
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Warning {

    /**
     * 警告id标识
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

    /**
     * 较长的一半/半径
     */
    private Integer semiMajor;

    /**
     * 较短的一半
     */
    private Integer semiMinor;

    /**
     * 警告范围类型
     */
    private Integer areaType;

    public Warning(SimpleDenm simpleDenm, Area area) {
        this.id = simpleDenm.stationId;
        this.longitude = simpleDenm.longitude / 1e7;
        this.latitude = simpleDenm.latitude / 1e7;
        this.semiMajor = simpleDenm.semiMajorConfidence;
        this.semiMinor = simpleDenm.semiMinorConfidence;
        this.areaType = area.type().code();
    }

}
