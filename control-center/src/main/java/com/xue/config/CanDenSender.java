package com.xue.config;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xue.cache.Region;
import com.xue.controller.VehicleWarningSocket;
import com.xue.entity.AlertEntity;
import com.xue.entity.CarEntity;
import com.xue.entity.Path;
import com.xue.frame.*;
import com.xue.mapper.AlertMapper;
import com.xue.mapper.CarMapper;
import com.xue.mapper.PathMapper;
import com.xue.utils.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import javax.annotation.PostConstruct;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Xue
 * @create 2024-04-22 15:43
 */
@Slf4j
@Component
@EnableAsync
@EnableScheduling
public class CanDenSender {

    private final GeoFrame geoFrame;
    private final AlertMapper alertMapper;
    private final CarMapper carMapper;
    private final PathMapper pathMapper;

    @Autowired
    public CanDenSender(GeoFrame geoFrame, AlertMapper alertMapper, CarMapper carMapper, PathMapper pathMapper) {
        this.geoFrame = geoFrame;
        this.alertMapper = alertMapper;
        this.carMapper = carMapper;
        this.pathMapper = pathMapper;
    }

    private volatile List<Warning> warnings = new ArrayList<>();
    private volatile List<Car> cars = new ArrayList<>();
    private final SecureRandom random = new SecureRandom();

    @PostConstruct
    public void init() {
        cacheWarningsSynchronization();
        cacheDummyCars();
    }

    private void cacheDummyCars() {
        LambdaQueryWrapper<CarEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CarEntity::getVirtualCar, true);
        cars = carMapper.selectList(wrapper).stream().map(carEntity -> {
            Car car = new Car(carEntity);
            car.setPath(generationPath());
            updateLocationStorageByPath(car);
            return car;
        }).collect(Collectors.toList());
    }

    public void cacheWarningsSynchronization() {
        List<AlertEntity> alertList = alertMapper.selectList(null);
        warnings = alertList.stream().map(Warning::new).collect(Collectors.toList());
    }

    public Path generationPath() {
        List<Integer> ids = pathMapper.selectId();
        Integer id = ids.get(random.nextInt(ids.size()));
        return pathMapper.selectById(id);
    }

    public Path getNextPath(Path path) {
        LambdaQueryWrapper<Path> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Path::getId, path.getId() + 1).eq(Path::getPathId, path.getPathId());
        Path rePath = pathMapper.selectOne(wrapper);
        if (ObjectUtils.isEmpty(rePath)) {
            return pathMapper.selectOneByPathIdOrderById(path.getPathId());
        }
        return rePath;
    }

    public void updateLocationStorageByPath(Car car) {
        Path path = car.getPath();
        car.setLatitude(path.getLatitude());
        car.setLongitude(path.getLongitude());
        carMapper.updateById(car.carEntityUseUpdate());
    }

    @Async
    @Scheduled(fixedRate = 1000)
    public void simulateCarRunning() {
        cars.forEach(car -> {
            Path nextPath = getNextPath(car.getPath());
            if (!ObjectUtils.isEmpty(nextPath)) {
                car.setPath(getNextPath(car.getPath()));
                updateLocationStorageByPath(car);
            }
        });
    }

    @Async
    @Scheduled(fixedRate = 1000)
    public void sendDenm() {
        warnings.forEach(warning -> {
            SimpleDenm simpleDenm = MessageFactory.getInstance().getDenm(warning);
            geoFrame.sendDenm(simpleDenm, true, warning.getType());
        });
    }

    @Async
    @Scheduled(fixedRate = 1000)
    public void synchronizeTrolleyWarningMessages() {
        List<Warning> warningsNow = Region.getInstance().getWarnings();
        List<Car> carsNow = Region.getInstance().getCars();
        carsNow.addAll(cars);
        warningsNow.addAll(warnings);
        HashMap<String, Object> sendMap = new HashMap<String, Object>() {
            {
                put("cars", carsNow);
                put("warnings", warningsNow.stream().distinct().collect(Collectors.toList()));
            }
        };
        VehicleWarningSocket.send(JsonUtil.toJSONString(sendMap));
    }


    public void addCar(CarEntity carEntity) {
        Car car = new Car(carEntity);
        car.setPath(generationPath());
        updateLocationStorageByPath(car);
        cars.add(car);
    }

    public void removeCar(Integer stationId) {
        cars.removeIf(car -> car.getStationId().equals(stationId));
    }

    public void update(CarEntity carEntity) {
        for (Car car : cars) {
            if (car.getStationId().equals(carEntity.getStationId())) {
                car.setDirection(carEntity.getDirection());
                car.setLength(carEntity.getLength());
                car.setWidth(carEntity.getWidth());
            }
        }
    }


}
