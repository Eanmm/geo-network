package com.xue;

import lombok.extern.slf4j.Slf4j;

/**
 * @author xuejingbao
 * @create 2023-05-24 16:18
 */
@Slf4j
public class GpsSender implements Runnable {

    @Override
    public void run() {
        Position position = GpsPosition.INSTANCE.getPosition();
        if (log.isDebugEnabled()){
            log.debug("获取位置信息：{}", position);
        }
        if (position != null) {
            UdpSender.sendMessage(MessageFactory.getCam(position).asByteArray());
            UdpSender.sendMessage(MessageFactory.getDenm(position).asByteArray());
        }
    }


}
