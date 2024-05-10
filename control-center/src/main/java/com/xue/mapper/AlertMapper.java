package com.xue.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xue.entity.AlertEntity;

import java.util.List;

public interface AlertMapper extends BaseMapper<AlertEntity> {
    void insertAlert(AlertEntity alertEntity);

    void delAlert(int stationId);

    AlertEntity getAlertById(int stationId);

    List<AlertEntity> getAlertAll();

    void updateAlertById(AlertEntity alertEntity);

    int insertIfNotExistenceOrUpdateIfExistence(AlertEntity alertEntity);

}
