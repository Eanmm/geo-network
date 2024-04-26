package com.xue.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xue.entity.AlertEntity;

import java.util.List;

public interface AlertService extends IService<AlertEntity> {

    void addAlert(AlertEntity alert);

    void delAlert(int stationId);

    List<AlertEntity> getAlertList();

    AlertEntity getAlertById(int stationId);

    void updateAlert(AlertEntity alert);
}
