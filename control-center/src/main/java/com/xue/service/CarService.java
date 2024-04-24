package com.xue.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xue.entity.CarEntity;

import java.util.List;

public interface CarService extends IService<CarEntity> {

    void addCar(CarEntity car);

    void delCar(int carId);

    List<CarEntity> getCarList();

    CarEntity getCarById(int carId);

    void updateCar(CarEntity car);

}
