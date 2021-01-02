package com.chanshiyu.chat.handler.request;

import com.chanshiyu.chat.handler.DisruptorRequestHandler;
import com.chanshiyu.chat.protocol.request.UpdateGroupRequestPacket;
import io.netty.channel.ChannelHandler;

/**
 * @author SHIYU
 * @description
 * @since 2021/1/2 15:33
 */
@ChannelHandler.Sharable
public class UpdateGroupRequestHandler extends DisruptorRequestHandler<UpdateGroupRequestPacket> {

    public static final UpdateGroupRequestHandler INSTANCE = new UpdateGroupRequestHandler();

    private UpdateGroupRequestHandler() {
    }

}