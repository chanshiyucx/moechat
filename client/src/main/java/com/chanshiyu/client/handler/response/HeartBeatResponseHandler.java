package com.chanshiyu.client.handler.response;

import com.chanshiyu.chat.protocol.response.HeartBeatResponsePacket;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * @author SHIYU
 * @description
 * @since 2020/11/11 11:32
 */
public class HeartBeatResponseHandler extends SimpleChannelInboundHandler<HeartBeatResponsePacket> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, HeartBeatResponsePacket responsePacket) {
    }

}
