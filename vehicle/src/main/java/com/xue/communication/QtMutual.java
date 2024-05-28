package com.xue.communication;

import com.xue.bean.Agreement;
import com.xue.utils.JsonUtil;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.DatagramPacket;
import io.netty.channel.socket.nio.NioDatagramChannel;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;

/**
 * @author Xue
 * @create 2024-04-28 14:25
 */
@Slf4j
public class QtMutual {

    private QtMutual() {
    }

    private static class QtMutualInstance {
        private static final QtMutual INSTANCE = new QtMutual();
    }

    public static QtMutual getInstance() {
        return QtMutualInstance.INSTANCE;
    }

    private static Channel channel;
    protected static volatile InetSocketAddress sendAddress;

    public void connect() throws InterruptedException {
        EventLoopGroup group = new NioEventLoopGroup();
        AgreementHandler agreementHandler = new AgreementHandler();
        VehicleStatusHandler vehicleStatusHandler = new VehicleStatusHandler();
        VehicleAlertsHandler vehicleAlertsHandler = new VehicleAlertsHandler();
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(group)
                .channel(NioDatagramChannel.class)
                // 广播
                .option(ChannelOption.SO_BROADCAST, true)
                // 设置udp单帧超过2M的办法
                //.option(ChannelOption.RCVBUF_ALLOCATOR, new FixedRecvByteBufAllocator(65535))
                // 设置读缓冲区为2M
                .option(ChannelOption.SO_RCVBUF, 2048 * 1024)
                // 设置写缓冲区为1M
                .option(ChannelOption.SO_SNDBUF, 1024 * 1024)
                .handler(new ChannelInitializer<NioDatagramChannel>() {
                    @Override
                    protected void initChannel(NioDatagramChannel ch) throws Exception {
                        ChannelPipeline pipeline = ch.pipeline();
                        pipeline.addLast(agreementHandler);
                        pipeline.addLast(vehicleStatusHandler);
                        pipeline.addLast(vehicleAlertsHandler);
                    }
                });
        ChannelFuture f = bootstrap.bind(9090).sync();
        log.info("QtMutual ready...");
        channel = f.channel();
        channel.closeFuture().addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture channelFuture) throws Exception {
                log.info("QtMutual shutDown...");
            }
        });
    }

    public static void sendMsg(Agreement agreement) {
        String message = JsonUtil.toJSONString(agreement);
        // log.info("QtMutual sendMsg:{}", message);
        ByteBuf buf = Unpooled.copiedBuffer(message, CharsetUtil.UTF_8);
        if (sendAddress != null && channel != null && channel.isActive()) {
            channel.writeAndFlush(new DatagramPacket(buf, sendAddress));
        }
    }


}
