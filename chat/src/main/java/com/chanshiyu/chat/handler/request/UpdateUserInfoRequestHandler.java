package com.chanshiyu.chat.handler.request;

import com.chanshiyu.chat.handler.DisruptorRequestHandler;
import com.chanshiyu.chat.protocol.request.UpdateUserInfoRequestPacket;
import io.netty.channel.ChannelHandler;

/**
 * @author SHIYU
 * @description
 * @since 2020/12/28 14:41
 */
@ChannelHandler.Sharable
public class UpdateUserInfoRequestHandler extends DisruptorRequestHandler<UpdateUserInfoRequestPacket> {

    public static final UpdateUserInfoRequestHandler INSTANCE = new UpdateUserInfoRequestHandler();

    private UpdateUserInfoRequestHandler() {
    }

}