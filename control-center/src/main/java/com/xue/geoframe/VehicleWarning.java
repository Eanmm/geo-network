package com.xue.geoframe;

import com.xue.bean.MessageFactory;
import com.xue.bean.Position;
import lombok.extern.slf4j.Slf4j;
import net.gcdc.geonetworking.Area;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * 车辆信息和警告信息综合管理
 *
 * @author Xue
 * @create 2024-04-23 13:34
 */
@Slf4j
@Component
public class VehicleWarning {

    private final MessageFactory messageFactory;

    @Autowired
    public VehicleWarning(MessageFactory messageFactory) {
        this.messageFactory = messageFactory;
    }

    private final ExecutorService executorService = Executors.newCachedThreadPool();
    private final ConcurrentHashMap<Integer, Car> carMap = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<Long, Warning> warningMap = new ConcurrentHashMap<>();

    @PostConstruct
    public void loadVirtualVehicleWarnings() {
        log.info("加载数据库虚拟警告和车辆信息");
        Warning warning = new Warning(messageFactory.getDenm(new Position(119.90259,30.265911),1,1),Area.circle(new net.gcdc.geonetworking.Position(119.90259,30.265911),1) , true);
        warningMap.put(warning.getId(),warning);
    }

    /**
     * 检查该信息是否存在，更新
     *
     * @param simpleCam
     */
    public void check(SimpleCam simpleCam) {
        Car car = new Car(simpleCam);
        carMap.put(car.getId(), car);
        executorService.submit(() -> {
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                log.error("定时删除Cam信息线程故障", e);
            }
            carMap.remove(car.getId());
        });
    }

    public void check(SimpleDenm simpleDenm, Area area) {
        Warning warning = new Warning(simpleDenm, area, false);
        warningMap.put(warning.getId(), warning);
        executorService.submit(() -> {
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                log.error("定时删除Denm信息线程故障", e);
            }
            warningMap.remove(warning.getId());
        });
    }

    public List<Car> getNearbyVehicles() {
        return new ArrayList<>(carMap.values());
    }

    public List<Warning> getWarnings() {
        return new ArrayList<>(warningMap.values());
    }

    public void addWarning(Warning warning) {
        warning.setSelf(true);
        warningMap.put(warning.getId(), warning);
    }

    public void removeWarningById(Long id) {
        warningMap.remove(id);
    }


}
