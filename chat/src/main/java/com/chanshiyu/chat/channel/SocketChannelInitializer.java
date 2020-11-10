package com.chanshiyu.chat.channel;

import com.chanshiyu.chat.protocol.Packet;
import com.chanshiyu.chat.protocol.PacketCodec;
import com.chanshiyu.chat.protocol.request.LoginRequestPacket;
import com.chanshiyu.chat.protocol.request.MessageRequestPacket;
import com.chanshiyu.chat.protocol.response.LoginResponsePacket;
import com.chanshiyu.chat.protocol.response.MessageResponsePacket;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.Date;

/**
 * @author SHIYU
 * @description
 * @since 2020/11/9 15:49
 */
public class SocketChannelInitializer extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {


//        ByteBuf requestByteBuf = (ByteBuf) msg;
//        Packet packet = PacketCodec.INSTANCE.decode(requestByteBuf);
//        if (packet instanceof LoginRequestPacket) {
//            // 登录流程
//            LoginRequestPacket loginRequestPacket = (LoginRequestPacket) packet;
//            LoginResponsePacket loginResponsePacket = new LoginResponsePacket();
//            loginResponsePacket.setVersion(packet.getVersion());
//            if (valid(loginRequestPacket)) {
//                loginResponsePacket.setSuccess(true);
//                System.out.println(new Date() + ": 登录成功!");
//            } else {
//                loginResponsePacket.setMessage("账号密码校验失败");
//                loginResponsePacket.setSuccess(false);
//                System.out.println(new Date() + ": 登录失败!");
//            }
//            // 登录响应
//            ByteBuf responseByteBuf = PacketCodec.INSTANCE.encode(ctx.alloc(), loginResponsePacket);
//            ctx.channel().writeAndFlush(responseByteBuf);
//        } else if (packet instanceof MessageRequestPacket) {
//            // 客户端发来消息
//            MessageRequestPacket messageRequestPacket = ((MessageRequestPacket) packet);
//            MessageResponsePacket messageResponsePacket = new MessageResponsePacket();
//            System.out.println(new Date() + ": 收到客户端消息: " + messageRequestPacket.getMessage());
//            messageResponsePacket.setMessage("服务端回复【" + messageRequestPacket.getMessage() + "】");
//            ByteBuf responseByteBuf = PacketCodec.INSTANCE.encode(ctx.alloc(), messageResponsePacket);
//            ctx.channel().writeAndFlush(responseByteBuf);
//        }
    }

    private boolean valid(LoginRequestPacket loginRequestPacket) {
        return true;
    }

}
