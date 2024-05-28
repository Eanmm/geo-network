package com.xue.communication;

import com.xue.bean.Agreement;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.DatagramPacket;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;

/**
 * @author Xue
 * @create 2024-04-28 15:05
 */
@Slf4j
@ChannelHandler.Sharable
public class AgreementHandler extends SimpleChannelInboundHandler<DatagramPacket> {

    private static final String HEARTBEAT = "heart beat";

    @Override
    protected void channelRead0(ChannelHandlerContext chc, DatagramPacket datagramPacket) throws Exception {
        if (QtMutual.sendAddress == null) {
            InetSocketAddress ipSocket = datagramPacket.sender();
            String hostAddress = ipSocket.getAddress().getHostAddress();
            int port = ipSocket.getPort();
            log.info("get qt client address:{}-{}", hostAddress, port);
            QtMutual.sendAddress = new InetSocketAddress(hostAddress, port);
        }
        ByteBuf buf = datagramPacket.content();
        byte[] bytes = new byte[buf.readableBytes()];
        buf.readBytes(bytes);
        String messageStr = new String(bytes, StandardCharsets.UTF_8);
        if (!HEARTBEAT.equals(messageStr)) {
            // log.info("receive message:{}", messageStr);
            Agreement agreement = Agreement.decode(messageStr);
            chc.fireChannelRead(agreement);
        }
    }

}
