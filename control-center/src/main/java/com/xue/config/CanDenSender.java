package com.xue.config;

import com.xue.cache.Region;
import com.xue.controller.VehicleWarningSocket;
import com.xue.entity.AlertEntity;
import com.xue.frame.GeoFrame;
import com.xue.frame.MessageFactory;
import com.xue.frame.SimpleDenm;
import com.xue.frame.Warning;
import com.xue.mapper.AlertMapper;
import com.xue.utils.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author Xue
 * @create 2024-04-22 15:43
 */
@Slf4j
@Component
@EnableAsync
@EnableScheduling
public class CanDenSender {

    private final GeoFrame geoFrame;
    private final AlertMapper alertMapper;

    @Autowired
    public CanDenSender(GeoFrame geoFrame, AlertMapper alertMapper) {
        this.geoFrame = geoFrame;
        this.alertMapper = alertMapper;
    }

    private CopyOnWriteArrayList<Warning> warnings = new CopyOnWriteArrayList<>();

    @PostConstruct
    public void init() {
        cacheSynchronization();
    }

    public void cacheSynchronization() {
        List<AlertEntity> alertList = alertMapper.selectList(null);
        CopyOnWriteArrayList<Warning> freshWarnings = new CopyOnWriteArrayList<>();
        alertList.forEach(alert -> {
            Warning warning = new Warning(alert);
            freshWarnings.add(warning);
        });
        warnings = freshWarnings;
    }


    @Async
    @Scheduled(fixedRate = 1000)
    public void sendDenm() {
        warnings.forEach(warning -> {
            SimpleDenm simpleDenm = MessageFactory.getInstance().getDenm(warning);
            geoFrame.sendDenm(simpleDenm, true, warning.getType());
            Region.getInstance().fetchWarning(warning);
        });
    }

    @Async
    @Scheduled(fixedRate = 1000)
    public void synchronizeTrolleyWarningMessages() {
        HashMap<String, Object> sendMap = new HashMap<String, Object>() {
            {
                put("cars", Region.getInstance().getCars());
                put("warnings", Region.getInstance().getWarnings());
            }
        };
        VehicleWarningSocket.send(JsonUtil.toJSONString(sendMap));
    }


}
