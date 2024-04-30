package com.xue.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xue.config.CanDenSender;
import com.xue.entity.AlertEntity;
import com.xue.mapper.AlertMapper;
import com.xue.service.AlertService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AlertServiceImpl extends ServiceImpl<AlertMapper, AlertEntity> implements AlertService {
    @Autowired
    private AlertMapper alertMapper;
    @Autowired
    private CanDenSender canDenSender;

    @Override
    public void addAlert(AlertEntity alert) {
        alertMapper.insertAlert(alert);
        canDenSender.cacheSynchronization();
    }

    @Override
    public void delAlert(int stationId) {
        alertMapper.delAlert(stationId);
        canDenSender.cacheSynchronization();
    }

    @Override
    public List<AlertEntity> getAlertList() {
        return alertMapper.getAlertAll();
    }

    @Override
    public AlertEntity getAlertById(int stationId) {
        return alertMapper.getAlertById(stationId);
    }

    @Override
    public void updateAlert(AlertEntity alert) {
        alertMapper.updateAlertById(alert);
        canDenSender.cacheSynchronization();
    }
}
