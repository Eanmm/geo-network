package com.xue.config;

import com.xue.cache.Region;
import com.xue.controller.VehicleWarningSocket;
import com.xue.entity.AlertEntity;
import com.xue.frame.*;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

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

    private volatile List<Warning> warnings = new ArrayList<>();
    private volatile List<Car> cars = new ArrayList<>();

    @PostConstruct
    public void init() {
        cacheWarningsSynchronization();
    }

    public void cacheWarningsSynchronization() {
        List<AlertEntity> alertList = alertMapper.selectList(null);
        warnings = alertList.stream().map(Warning::new).collect(Collectors.toList());
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
        List<Warning> warningsNow = Region.getInstance().getWarnings();
        List<Car> carsNow = Region.getInstance().getCars();
        carsNow.addAll(cars);
        warningsNow.addAll(warnings);
        HashMap<String, Object> sendMap = new HashMap<String, Object>() {
            {
                put("cars", carsNow);
                put("warnings", warningsNow.stream().distinct().collect(Collectors.toList()));
            }
        };
        VehicleWarningSocket.send(JsonUtil.toJSONString(sendMap));
    }


}
