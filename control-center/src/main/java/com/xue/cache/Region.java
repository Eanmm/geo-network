package com.xue.cache;

import com.xue.frame.Car;
import com.xue.frame.Warning;
import com.xue.mapper.AlertMapper;
import com.xue.utils.SpringUtils;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * @author Xue
 * @create 2024-04-28 15:43
 */
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
    private final ConcurrentHashMap<Integer, WarningExpand> warningExpMap = new ConcurrentHashMap<>();

    private final AlertMapper alertMapper = SpringUtils.getBean(AlertMapper.class);


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
        WarningExpand warningExp = warningExpMap.get(stationId);
        if (warningExp == null) {
            warningExpMap.put(stationId, new WarningExpand(
                    warning,
                    () -> warningExpMap.remove(stationId)
            ));
            // 收到警告，添加入库
            alertMapper.insertIfNotExistenceOrUpdateIfExistence(warning.toAlertEntity());
        } else {
            if (!warningExp.getWarning().equals(warning)) {
                warningExp.setWarning(warning);
                // 警告改变，更新入库
                alertMapper.insertIfNotExistenceOrUpdateIfExistence(warning.toAlertEntity());
            }
            warningExp.delayLife();
        }
    }

    public List<Warning> getWarnings() {
        return warningExpMap.values().stream().map(WarningExpand::getWarning).collect(Collectors.toList());
    }

}






