package com.xue.config;

import com.xue.bean.MessageFactory;
import com.xue.bean.Position;
import com.xue.geoframe.GeoFrame;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

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

    @Autowired
    public CanDenSender(GeoFrame geoFrame, MessageFactory messageFactory) {
        this.geoFrame = geoFrame;
        this.messageFactory = messageFactory;
    }

    @Async
    @Scheduled(fixedRate = 1000)
    public void sendCam() {
        geoFrame.sendCam(messageFactory.getCam(new Position(1.0, 2.0)));
    }


}
