package com.xue.cache;

import com.xue.frame.Car;
import com.xue.frame.Warning;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * @author Xue
 * @create 2024-04-28 15:43
 */
@Slf4j
public class Region {

    private Region() {
    }

    private static class RegionInstance {
        private static final Region INSTANCE = new Region();
    }

    public static Region getInstance() {
        return RegionInstance.INSTANCE;
    }

    private final ConcurrentHashMap<Integer, CarExpand> carExpandMap = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<Integer, WarningExpand> warningExpandMap = new ConcurrentHashMap<>();


    public void fetchCar(Car car) {
        Integer stationId = car.getStationId();
        CarExpand carExpand = carExpandMap.get(stationId);
        if (carExpand == null) {
            carExpandMap.put(stationId, new CarExpand(
                    car,
                    () -> carExpandMap.remove(stationId)
            ));
        } else {
            carExpand.setCar(car);
            carExpand.delayLife();
        }
    }

    public List<Car> getCars() {
        return carExpandMap.values().stream().map(CarExpand::getCar).collect(Collectors.toList());
    }

    public void fetchWarning(Warning warning) {
        Integer stationId = warning.getStationId();
        WarningExpand warningExpand = warningExpandMap.get(stationId);
        if (warningExpand == null) {
            warningExpandMap.put(stationId, new WarningExpand(
                    warning,
                    () -> warningExpandMap.remove(stationId)
            ));
        } else {
            warningExpand.setWarning(warning);
            warningExpand.delayLife();
        }
    }

    public List<Warning> getWarnings() {
        return warningExpandMap.values().stream().map(WarningExpand::getWarning).collect(Collectors.toList());
    }

}






