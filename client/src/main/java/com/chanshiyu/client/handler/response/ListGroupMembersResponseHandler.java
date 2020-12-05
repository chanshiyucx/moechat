package com.chanshiyu.client.handler.response;

import com.chanshiyu.chat.protocol.response.ListMembersResponsePacket;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * @author SHIYU
 * @description
 * @since 2020/11/10 17:52
 */
public class ListGroupMembersResponseHandler extends SimpleChannelInboundHandler<ListMembersResponsePacket> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ListMembersResponsePacket responsePacket) {
        System.out.println("群[" + responsePacket.getGroupId() + "]中的人包括：" + responsePacket.getSessionList());
    }

}