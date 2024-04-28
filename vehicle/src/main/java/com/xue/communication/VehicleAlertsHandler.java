package com.xue.communication;

import com.xue.arrangement.WarningSettings;
import com.xue.bean.VehicleAlerts;
import com.xue.frame.Warning;
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
public class VehicleAlertsHandler extends SimpleChannelInboundHandler<VehicleAlerts> {

    private final static WarningSettings warningSettings = WarningSettings.getInstance();

    @Override
    protected void channelRead0(ChannelHandlerContext chc, VehicleAlerts vehicleAlerts) throws Exception {
        Warning warning = warningSettings.getWarningByNear(vehicleAlerts);

    }

}
