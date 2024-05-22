package com.xue.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xue.entity.CarEntity;

import java.util.List;

public interface CarService extends IService<CarEntity> {

    void addCar(CarEntity car);

    void delCar(Integer stationId);

    List<CarEntity> getCarList(Integer stationId);

    void updateCar(CarEntity car);

}
