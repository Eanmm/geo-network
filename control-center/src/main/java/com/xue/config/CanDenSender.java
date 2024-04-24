package com.xue.config;

import cn.hutool.json.JSONUtil;
import com.xue.bean.MessageFactory;
import com.xue.bean.Position;
import com.xue.controller.VehicleWarningSocket;
import com.xue.geoframe.GeoFrame;
import com.xue.geoframe.VehicleWarning;
import com.xue.geoframe.Warning;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;

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
    private final MessageFactory messageFactory;

    private final VehicleWarning vehicleWarning;

    @Autowired
    public CanDenSender(GeoFrame geoFrame, MessageFactory messageFactory, VehicleWarning vehicleWarning) {
        this.geoFrame = geoFrame;
        this.messageFactory = messageFactory;
        this.vehicleWarning = vehicleWarning;
    }

    @Async
    @Scheduled(fixedRate = 1000)
    public void sendCam() {
        // geoFrame.sendCam(messageFactory.getCam(new Position(119.90259, 30.265911)));
    }

    @Async
    @Scheduled(fixedRate = 1000)
    public void sendDenm() {
        List<Warning> warnings = vehicleWarning.getWarnings();
        warnings.stream().filter(Warning::getSelf).forEach(warning -> {
            geoFrame.sendDenm(
                    messageFactory.getDenm(
                            new Position(warning.getLongitude(), warning.getLatitude()),
                            warning.getSemiMajor(),
                            warning.getSemiMinor()
                    ),
                    true,
                    warning.getAreaType()
            );
        });
    }

    @Async
    @Scheduled(fixedRate = 1000)
    public void synchronizeTrolleyWarningMessages() {
        HashMap<String, Object> sendMap = new HashMap<String, Object>() {
            {
                put("nearbyVehicles", vehicleWarning.getNearbyVehicles());
                put("warnings", vehicleWarning.getWarnings());
            }
        };
        VehicleWarningSocket.send(JSONUtil.toJsonStr(sendMap));
    }


}
