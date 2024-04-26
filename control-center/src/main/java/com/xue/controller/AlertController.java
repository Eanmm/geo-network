package com.xue.controller;

import com.xue.config.Result;
import com.xue.entity.AlertEntity;
import com.xue.service.AlertService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "告警管理")
@RestController
@RequestMapping("/smart-traffic")
public class AlertController {
    @Autowired
    private AlertService alertService;

    @ApiOperation("添加告警信息")
    @PostMapping("/add-alert")
    public Result addAlert(
            @ApiParam(name = "AlertEntity", required = true, value = "告警信息")
            @RequestBody AlertEntity alert
    ) {
        alertService.addAlert(alert);
        return new Result().success(null);
    }

    @ApiOperation("删除告警信息")
    @DeleteMapping("/del-alert")
    public Result delCar(
            @RequestParam(name = "stationId", required = true)
            @ApiParam(value = "告警ID")
                    int stationId
    ){
        alertService.delAlert(stationId);
        return new Result().success(null);
    }

    @ApiOperation("精确查询告警信息")
    @GetMapping("/get-alert")
    AlertEntity getCarById(
            @RequestParam(name = "stationId", required = true)
            @ApiParam(value = "告警ID")
                    int stationId
    ){
        return alertService.getAlertById(stationId);
    }

    @ApiOperation("查询全部告警信息")
    @GetMapping("/get-alert/list")
    List<AlertEntity> getAlertList(){
        return alertService.getAlertList();
    }

    @ApiOperation("修改告警信息")
    @PostMapping("/update-alert")
    public Result updateAlert(@ApiParam(name = "AlertEntity", required = true, value = "告警信息")
                              @RequestBody AlertEntity alert){
        alertService.updateAlert(alert);
        return new Result().success(null);
    }

}
