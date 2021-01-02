package com.chanshiyu.chat.handler.request;

import com.chanshiyu.chat.handler.DisruptorRequestHandler;
import com.chanshiyu.chat.protocol.request.UpdateUserRequestPacket;
import io.netty.channel.ChannelHandler;

/**
 * @author SHIYU
 * @description
 * @since 2020/12/28 14:41
 */
@ChannelHandler.Sharable
public class UpdateUserRequestHandler extends DisruptorRequestHandler<UpdateUserRequestPacket> {

    public static final UpdateUserRequestHandler INSTANCE = new UpdateUserRequestHandler();

    private UpdateUserRequestHandler() {
    }

}