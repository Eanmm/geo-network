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
                    Integer stationId
    ){
        alertService.delAlert(stationId);
        return new Result().success(null);
    }

    @ApiOperation("查询告警信息")
    @GetMapping("/get-alert/list")
    List<AlertEntity> getAlertList(
            @RequestParam(name = "stationId", required = false)
            @ApiParam(value = "告警ID")
                    Integer stationId
    ){
        return alertService.getAlertList(stationId);
    }

    @ApiOperation("修改告警信息")
    @PostMapping("/update-alert")
    public Result updateAlert(@ApiParam(name = "AlertEntity", required = true, value = "告警信息")
                              @RequestBody AlertEntity alert){
        alertService.updateAlert(alert);
        return new Result().success(null);
    }

}
