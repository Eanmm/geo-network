package com.xue.communication;

import com.xue.Region;
import com.xue.arrangement.WarningSettings;
import com.xue.bean.VehicleAlerts;
import com.xue.frame.GeoFrame;
import com.xue.frame.MessageFactory;
import com.xue.frame.SimpleDenm;
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
        // 这里要周期性发送，而且当有新的来需要停止，如果距离当前gps较远，也需要停止

        Region.getInstance().fetchWarning(warning);
        SimpleDenm denm = MessageFactory.getInstance().getDenm(warning);
        GeoFrame.getInstance().sendDenm(denm, true, warning.getType());
    }

}
