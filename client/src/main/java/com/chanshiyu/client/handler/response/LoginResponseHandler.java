package com.chanshiyu.client.handler.response;

import com.chanshiyu.chat.protocol.response.LoginResponsePacket;
import com.chanshiyu.chat.session.Session;
import com.chanshiyu.chat.util.SessionUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * @author SHIYU
 * @description
 * @since 2020/11/10 9:22
 */
public class LoginResponseHandler extends SimpleChannelInboundHandler<LoginResponsePacket> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, LoginResponsePacket loginResponsePacket) {
        int userId = loginResponsePacket.getUserId();
        String userName = loginResponsePacket.getUsername();
        if (loginResponsePacket.isSuccess()) {
            System.out.println("[" + userName + "]登录成功，userId 为: " + loginResponsePacket.getUserId());
            Session session = new Session();
            session.setUserId(userId);
            session.setUsername(userName);
            SessionUtil.bindSession(session, ctx.channel());
        } else {
            System.out.println("[" + userName + "]登录失败，原因：" + loginResponsePacket.getMessage());
        }
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        System.out.println("客户端连接被关闭!");
    }

}
