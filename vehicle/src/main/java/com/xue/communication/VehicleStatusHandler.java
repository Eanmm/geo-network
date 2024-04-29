package com.xue.communication;

import com.xue.Region;
import com.xue.bean.VehicleStatus;
import com.xue.frame.*;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Xue
 * @create 2024-04-28 15:39
 */
@Slf4j
@ChannelHandler.Sharable
public class VehicleStatusHandler extends SimpleChannelInboundHandler<VehicleStatus> {

    @Override
    protected void channelRead0(ChannelHandlerContext chc, VehicleStatus vehicleStatus) throws Exception {
        Car car = new Car(vehicleStatus);
        Warning warning = new Warning(car);
        // 记录自身的位置和警告信息
        Region.getInstance().fetchCar(car);
        Region.getInstance().fetchWarning(warning);
        // 发送至链路层
        SimpleCam cam = MessageFactory.getInstance().getCam(car);
        GeoFrame.getInstance().sendCam(cam);
        // 发送自身警告信息
        SimpleDenm denm = MessageFactory.getInstance().getDenm(warning);
        GeoFrame.getInstance().sendDenm(denm, true, warning.getType());
    }

}
