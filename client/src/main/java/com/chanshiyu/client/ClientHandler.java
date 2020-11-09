package com.chanshiyu.client;

import com.chanshiyu.chat.protocol.Packet;
import com.chanshiyu.chat.protocol.PacketCodec;
import com.chanshiyu.chat.protocol.request.LoginRequestPacket;
import com.chanshiyu.chat.protocol.response.LoginResponsePacket;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.Date;

/**
 * @author SHIYU
 * @description
 * @since 2020/11/9 15:42
 */
public class ClientHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        System.out.println(new Date() + ": 客户端开始登录");
        // 创建登录对象
        LoginRequestPacket loginRequestPacket = new LoginRequestPacket();
        loginRequestPacket.setUserId(123456);
        loginRequestPacket.setUsername("shiyu");
        // 编码
        ByteBuf buffer = PacketCodec.INSTANCE.encode(ctx.alloc(), loginRequestPacket);
        // 写数据
        ctx.channel().writeAndFlush(buffer);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        ByteBuf byteBuf = (ByteBuf) msg;
        Packet packet = PacketCodec.INSTANCE.decode(byteBuf);
        if (packet instanceof LoginResponsePacket) {
            LoginResponsePacket loginResponsePacket = (LoginResponsePacket) packet;
            if (loginResponsePacket.isSuccess()) {
                System.out.println(new Date() + ": 客户端登录成功");
            } else {
                System.out.println(new Date() + ": 客户端登录失败，原因：" + loginResponsePacket.getMessage());
            }
        }
    }

}