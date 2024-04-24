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
    @PostMapping("/del-alert")
    public Result delCar(
            @RequestParam(name = "alertId", required = true)
            @ApiParam(value = "告警ID")
                    int alertId
    ){
        alertService.delAlert(alertId);
        return new Result().success(null);
    }

    @ApiOperation("精确查询告警信息")
    @PostMapping("/get-alert")
    AlertEntity getCarById(
            @RequestParam(name = "alertId", required = true)
            @ApiParam(value = "告警ID")
                    int alertId
    ){
        return alertService.getAlertById(alertId);
    }

    @ApiOperation("查询全部告警信息")
    @PostMapping("/get-alert/list")
    List<AlertEntity> getAlertList(){
        return alertService.getAlertList();
    }

}
