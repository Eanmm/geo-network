package com.xue.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xue.config.CanDenSender;
import com.xue.entity.CarEntity;
import com.xue.mapper.CarMapper;
import com.xue.service.CarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CarServiceImpl extends ServiceImpl<CarMapper, CarEntity> implements CarService {
    @Autowired
    private CarMapper carMapper;
    @Autowired
    private CanDenSender canDenSender;

    @Override
    public void addCar(CarEntity car) {
        carMapper.insert(car);
        canDenSender.addCar(car);
    }

    @Override
    public void delCar(int stationId) {
        carMapper.delCar(stationId);
        canDenSender.removeCar(stationId);
    }

    @Override
    public List<CarEntity> getCarList() {
        return carMapper.getCarAll();
    }

    @Override
    public CarEntity getCarById(int stationId) {
        return carMapper.getCarById(stationId);
    }

    @Override
    public void updateCar(CarEntity car) {
        carMapper.updateCarById(car);
        canDenSender.update(car);
    }
}
