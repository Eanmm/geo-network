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

    @Override
    protected void channelRead0(ChannelHandlerContext chc, DatagramPacket datagramPacket) throws Exception {
        if (QtMutual.sendAddress == null) {
            InetSocketAddress ipSocket = (InetSocketAddress) chc.channel().remoteAddress();
            String hostAddress = ipSocket.getAddress().getHostAddress();
            log.info("get qt client address:{}", hostAddress);
            String[] adArr = hostAddress.split(":");
            QtMutual.sendAddress = new InetSocketAddress(adArr[0], Integer.parseInt(adArr[1]));
        }
        ByteBuf buf = datagramPacket.content();
        byte[] bytes = new byte[buf.readableBytes()];
        buf.readBytes(bytes);

        Agreement agreement = Agreement.decode(new String(bytes, StandardCharsets.UTF_8));
        chc.fireChannelRead(agreement);
    }

}
