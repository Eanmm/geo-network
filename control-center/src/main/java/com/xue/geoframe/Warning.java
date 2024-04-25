package com.xue.geoframe;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import net.gcdc.geonetworking.Area;

import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author Xue
 * @create 2024-04-23 13:59
 */
@Slf4j
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Warning {

    /**
     * 警告id标识
     */
    private Long id;

    /**
     * 小车id
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

    /**
     * 是否是自己发送的
     */
    private Boolean self = true;

    private ExecutorService pool = Executors.newSingleThreadExecutor();
    /**
     * 动态移除标识
     */
    private AtomicBoolean retain = new AtomicBoolean(false);

    public Warning(SimpleDenm simpleDenm, Area area, Boolean self) {
        this.stationId = simpleDenm.stationId;
        this.longitude = simpleDenm.longitude / 1e7;
        this.latitude = simpleDenm.latitude / 1e7;
        this.semiMajor = simpleDenm.semiMajorConfidence;
        this.semiMinor = simpleDenm.semiMinorConfidence;
        this.areaType = area.type().code();
        this.self = self;
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
            VehicleWarning.removeWarning(this);
        });
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Warning warning = (Warning) o;
        return Objects.equals(id, warning.id) && Objects.equals(stationId, warning.stationId) && Objects.equals(longitude, warning.longitude) && Objects.equals(latitude, warning.latitude) && Objects.equals(semiMajor, warning.semiMajor) && Objects.equals(semiMinor, warning.semiMinor) && Objects.equals(areaType, warning.areaType) && Objects.equals(self, warning.self);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, stationId, longitude, latitude, semiMajor, semiMinor, areaType, self);
    }
}
