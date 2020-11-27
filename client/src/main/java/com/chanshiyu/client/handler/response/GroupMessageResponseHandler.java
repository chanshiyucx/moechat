package com.chanshiyu.client.handler.response;

import com.chanshiyu.chat.session.Session;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * @author SHIYU
 * @description
 * @since 2020/11/11 9:27
 */
public class GroupMessageResponseHandler extends SimpleChannelInboundHandler<GroupMessageResponsePacket> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, GroupMessageResponsePacket responsePacket) {
        int fromGroupId = responsePacket.getFromGroupId();
        Session fromUser = responsePacket.getFromUser();
        System.out.println("收到群[" + fromGroupId + "]中[" + fromUser + "]发来的消息：" + responsePacket.getMessage());
    }

}