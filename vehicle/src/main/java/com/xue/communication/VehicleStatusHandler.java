package com.xue.communication;

import com.xue.bean.VehicleStatus;
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

    }

}
