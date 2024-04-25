package com.xue.geoframe;

import com.xue.bean.CamMark;
import com.xue.bean.MessageFactory;
import lombok.extern.slf4j.Slf4j;
import net.gcdc.geonetworking.Area;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Collectors;

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

    private static final ConcurrentHashMap<CamMark, Car> carMap = new ConcurrentHashMap<>();
    private static final ConcurrentHashMap<Integer, CopyOnWriteArrayList<Warning>> warningMap = new ConcurrentHashMap<>();

    @PostConstruct
    public void loadVirtualVehicleWarnings() {
        ScheduledExecutorService pool = Executors.newScheduledThreadPool(1);
        pool.scheduleAtFixedRate(()->{
            carMap.forEach((k,v)->{
                if (!k.getMock() && !k.getDelay()) {
                    carMap.remove(k);
                }
            });
        }, 0,2, TimeUnit.SECONDS);


        log.info("加载数据库虚拟警告和车辆信息");
        // Warning warning = new Warning(messageFactory.getDenm(new Position(119.90259,30.265911),1,1),Area.circle(new net.gcdc.geonetworking.Position(119.90259,30.265911),1) , true);
        // warningMap.put(warning.getId(),warning);
    }

    /**
     * 检查该信息是否存在，更新
     *
     * @param simpleCam
     */
    public void check(SimpleCam simpleCam) {
        Car car = new Car(simpleCam);
        carMap.put(new CamMark(car.getStationId(),true), car);
    }

    public void check(SimpleDenm simpleDenm, Area area) {
        Warning warning = new Warning(simpleDenm, area, false);
        // 如果没有则执行函数的创建方法
        if (addWarning(warning)) {
            warning.automaticallyDies();
        }
    }

    public List<Car> getNearbyVehicles() {
        return new ArrayList<>(carMap.values());
    }

    public List<Warning> getWarnings() {
        return warningMap.values().stream().flatMap(List::stream).collect(Collectors.toList());
    }

    public boolean addWarning(Warning warning) {
        CopyOnWriteArrayList<Warning> warnings = warningMap.computeIfAbsent(warning.getStationId(), stationId -> new CopyOnWriteArrayList<>());
        if (warning.getSelf()) {
            warnings.add(warning);
            return true;
        }
        int index = warnings.indexOf(warning);
        if (index == -1) {
            warnings.add(warning);
            return true;
        } else {
            warnings.get(index).prolong();
            return false;
        }
    }

    public void addCar(Car car) {
        carMap.put(new CamMark(car.getStationId(),true),car);
    }

    public void removeWarningById(Long id) {
        warningMap.forEach((k, v) -> {
            v.forEach(warning -> {
                if (warning.getId().equals(id)) {
                    v.remove(warning);
                }
            });
        });
    }

    public static void removeWarning(Warning warning) {
        warningMap.forEach((k, v) -> {
            v.remove(warning);
        });
    }

    public static void removeCarById(Integer stationId) {
        carMap.remove(new CamMark(stationId));
    }


}
