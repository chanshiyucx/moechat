package com.chanshiyu.client.handler.response;

import com.chanshiyu.chat.protocol.response.ErrorOperationResponsePacket;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * @author SHIYU
 * @description
 * @since 2020/11/16 15:50
 */
public class ErrorOperationResponseHandler extends SimpleChannelInboundHandler<ErrorOperationResponsePacket> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ErrorOperationResponsePacket errorOperationResponsePacket) {
        System.out.println("响应错误：" + errorOperationResponsePacket.getMessage());
    }

}
