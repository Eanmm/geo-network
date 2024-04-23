package com.xue;

import com.alibaba.fastjson.JSONObject;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * @author xuejingbao
 * @create 2023-09-25 9:49
 */
@Slf4j
@ChannelHandler.Sharable
public class GpsGetterHandler extends SimpleChannelInboundHandler<ByteBuf> {

    private static ChannelGroup channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);


    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        channelGroup.add(ctx.channel());
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        channelGroup.remove(ctx.channel());
    }


    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf) throws Exception {
        if (byteBuf.readableBytes() == 27) {
            byte[] message = new byte[byteBuf.readableBytes()];
            byteBuf.readBytes(message);

            String json = new String(message, StandardCharsets.UTF_8);
            if (log.isDebugEnabled()) {
                log.debug("收到json信息：" + json);
            }
            List list = JSONObject.parseArray(json, List.class).get(0);
            Double lon = Double.parseDouble(list.get(0).toString());
            Double lat = Double.parseDouble(list.get(1).toString());
            log.info("获取小车位置信息,经度: {},纬度: {}", lon, lat);

            GpsPosition.INSTANCE.setPosition(new Position(lon, lat));
        }
    }

    public static void sendAllChannel(byte[] bytes) {
        ByteBuf buffer = ByteBufAllocator.DEFAULT.buffer();
        buffer.writeBytes(bytes);
        for (Channel channel : channelGroup) {
            channel.writeAndFlush(buffer);
        }
    }


}
