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
        canDenSender.cacheWarningsSynchronization();
    }

    @Override
    public void delAlert(Integer stationId) {
        alertMapper.delAlert(stationId);
        canDenSender.cacheWarningsSynchronization();
    }

    @Override
    public List<AlertEntity> getAlertList(Integer stationId) {
        if (stationId != null) {
            return alertMapper.getAlertById(stationId);
        } else {
            return alertMapper.getAlertAll();
        }
    }

    @Override
    public void updateAlert(AlertEntity alert) {
        alertMapper.updateAlertById(alert);
        canDenSender.cacheWarningsSynchronization();
    }
}
