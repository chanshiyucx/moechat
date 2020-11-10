package com.chanshiyu.chat.handler.request;

import com.chanshiyu.chat.protocol.request.MessageRequestPacket;
import com.chanshiyu.chat.protocol.response.MessageResponsePacket;
import com.chanshiyu.chat.session.Session;
import com.chanshiyu.chat.util.SessionUtil;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.Date;

/**
 * @author SHIYU
 * @description
 * @since 2020/11/10 9:04
 */
public class MessageRequestHandler extends SimpleChannelInboundHandler<MessageRequestPacket> {

//    public static final MessageRequestHandler INSTANCE = new MessageRequestHandler();
//
//    private MessageRequestHandler() {}

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MessageRequestPacket messageRequestPacket) {
        System.out.println("接收到数据包 ==> " + messageRequestPacket);
        // 1.拿到消息发送方的会话信息
        Session session = SessionUtil.getSession(ctx.channel());
        // 2.通过消息发送方的会话信息构造要发送的消息
        MessageResponsePacket messageResponsePacket = new MessageResponsePacket();
        messageResponsePacket.setFromUserId(session.getUserId());
        messageResponsePacket.setFromUsername(session.getUserName());
        messageResponsePacket.setMessage(messageRequestPacket.getMessage());
        // 3.拿到消息接收方的 channel
        Channel toUserChannel = SessionUtil.getChannel(messageRequestPacket.getToUserId());
        // 4.将消息发送给消息接收方
        if (toUserChannel != null && SessionUtil.hasLogin(toUserChannel)) {
            toUserChannel.writeAndFlush(messageResponsePacket);
        } else {
            System.err.println("[" + messageRequestPacket.getToUserId() + "] 不在线，发送失败!");
        }
    }

}
