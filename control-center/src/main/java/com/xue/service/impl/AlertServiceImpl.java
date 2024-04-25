package com.xue.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
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

    @Override
    public void addAlert(AlertEntity alert) {
        alertMapper.insertAlert(alert);
    }

    @Override
    public void delAlert(int alertId) {
        alertMapper.delAlert(alertId);
    }

    @Override
    public List<AlertEntity> getAlertList() {
        return alertMapper.getAlertAll();
    }

    @Override
    public AlertEntity getAlertById(int alertId) {
        return alertMapper.getAlertById(alertId);
    }

    @Override
    public void updateAlert(AlertEntity alert) {
        alertMapper.updateAlertById(alert);
    }
}
