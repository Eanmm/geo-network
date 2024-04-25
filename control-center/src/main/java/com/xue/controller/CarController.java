package com.xue.controller;

import com.xue.config.Result;
import com.xue.entity.CarEntity;
import com.xue.service.CarService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "车辆管理")
@RestController
@RequestMapping("/smart-traffic")
public class CarController {
    @Autowired
    private CarService carService;

    @ApiOperation("添加车辆信息")
    @PostMapping("/add-car")
    public Result addCar(
            @ApiParam(name = "CarEntity", required = true, value = "车辆信息")
            @RequestBody CarEntity car
    ) {
        carService.addCar(car);
        return new Result().success(null);
    }

    @ApiOperation("删除车辆信息")
    @PostMapping("/del-car")
    public Result delCar(
            @RequestParam(name = "carId", required = true)
            @ApiParam(value = "车辆ID")
                    int carId
    ){
        carService.delCar(carId);
        return new Result().success(null);
    }

    @ApiOperation("精确查询车辆信息")
    @PostMapping("/get-car")
    CarEntity getCarById(
            @RequestParam(name = "carId", required = true)
            @ApiParam(value = "车辆ID")
                    int carId
    ){
        return carService.getCarById(carId);
    }

    @ApiOperation("查询全部车辆信息")
    @PostMapping("/get-car/list")
    List<CarEntity> getCarList(){
        return carService.getCarList();
    }

    @ApiOperation("修改车辆信息")
    @PostMapping("/update-car")
    public Result updateCar(
            @ApiParam(name = "CarEntity", required = true, value = "车辆信息")
            @RequestBody CarEntity car
    ) {
        carService.updateCar(car);
        return new Result().success(null);
    }
}