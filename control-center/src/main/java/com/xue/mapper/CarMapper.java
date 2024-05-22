package com.xue.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xue.entity.CarEntity;

import java.util.List;

public interface CarMapper extends BaseMapper<CarEntity> {
    void insertCar(CarEntity carEntity);

    void delCar(int stationId);

    List<CarEntity> getCarById(int stationId);

    List<CarEntity> getCarAll();

    void updateCarById(CarEntity carEntity);

}