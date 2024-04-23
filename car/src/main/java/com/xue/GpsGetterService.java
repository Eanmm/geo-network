package com.xue;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.extern.slf4j.Slf4j;

/**
 * @author xuejingbao
 * @create 2023-09-25 9:48
 */
@Slf4j
public enum GpsGetterService {
    INSTANCE;

    private EventLoopGroup bossGroup;
    private EventLoopGroup workGroup;

    private static Channel channel;

    public void init() throws InterruptedException {
        bossGroup = new NioEventLoopGroup(1);
        workGroup = new NioEventLoopGroup(2);
        ServerBootstrap bootstrap = new ServerBootstrap();
        GpsGetterHandler gpsGetterHandler = new GpsGetterHandler();
        bootstrap.group(bossGroup, workGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel nioSocketChannel) throws Exception {
                        ChannelPipeline pipeline = nioSocketChannel.pipeline();
                        pipeline.addLast("GPS工作线程", gpsGetterHandler);
                    }
                });
        ChannelFuture channelFuture = bootstrap.bind(Config.INSTANCE.getGpsTcpPort()).sync();
        log.info("GPSServer启动成功，端口是：{}", Config.INSTANCE.getGpsTcpPort());
        channel = channelFuture.channel();
        channel.closeFuture().addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture channelFuture) throws Exception {
                log.info("GPSServer已关闭，端口是：{}", Config.INSTANCE.getGpsTcpPort());
            }
        });
    }


    public void stop() {
        bossGroup.shutdownGracefully();
        workGroup.shutdownGracefully();
    }
}
