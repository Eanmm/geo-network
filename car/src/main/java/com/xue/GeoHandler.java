package com.xue;

import com.alibaba.fastjson.JSONObject;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.DatagramPacket;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

/**
 * @author xuejingbao
 * @create 2023-05-24 10:25
 */
@Slf4j
@ChannelHandler.Sharable
public class GeoHandler extends SimpleChannelInboundHandler<DatagramPacket> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, DatagramPacket msg) throws Exception {
        ByteBuf content = msg.content();
        byte aByte = content.getByte(0);
        int readableBytes = content.readableBytes();
        if (readableBytes > 101) {
            if (log.isDebugEnabled()) {
                log.debug("不符合标准长度包，丢弃：{}", ByteBufUtil.hexDump(content));
            }
            return;
        }
        byte[] contentByte = new byte[readableBytes];
        content.readBytes(contentByte);
        switch (aByte) {
            case 1:
                SimpleDenm simpleDenm = new SimpleDenm(contentByte);
                if (!Config.INSTANCE.getStationId().equals(simpleDenm.stationId)) {
                    double lon = simpleDenm.longitude / 1e7;
                    double lat = simpleDenm.latitude / 1e7;
                    log.info("收到别人的denm，转向:{},{}", lon, lat);
                    // LightControl.INSTANCE.lightClose();
                    Position position = GpsPosition.INSTANCE.getPosition();
                    if (position.getLongitude() == null) {
                        return;
                    }
                    List reLs = Arrays.asList(
                            1,
                            simpleDenm.semiMajorConfidence,
                            position.getLongitude(),
                            position.getLatitude(),
                            lon,
                            lat
                    );
                    byte[] reBytes = JSONObject.toJSONString(reLs).getBytes(StandardCharsets.UTF_8);
                    GpsGetterHandler.sendAllChannel(reBytes);
                } else {
                    if (log.isDebugEnabled()) {
                        log.debug("收到自己的denm:{},{}", simpleDenm.longitude, simpleDenm.latitude);
                    }
                }
                break;
            case 2:
                SimpleCam simpleCam = new SimpleCam(contentByte);
                if (log.isDebugEnabled()) {
                    if (!Config.INSTANCE.getStationId().equals(simpleCam.stationId)) {
                        log.debug("收到自己的cam:{},{}", simpleCam.longitude, simpleCam.latitude);
                    } else {
                        log.debug("收到别人的cam:{},{}", simpleCam.longitude, simpleCam.latitude);
                    }
                }
                break;
            default:
                break;
        }


    }

}
