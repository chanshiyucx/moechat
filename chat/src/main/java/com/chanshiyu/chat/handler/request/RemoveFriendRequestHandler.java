package com.chanshiyu.chat.handler.request;

import com.chanshiyu.chat.handler.DisruptorRequestHandler;
import com.chanshiyu.chat.protocol.request.RemoveFriendRequestPacket;
import io.netty.channel.ChannelHandler;

/**
 * @author SHIYU
 * @description
 * @since 2020/12/4 11:01
 */
@ChannelHandler.Sharable
public class RemoveFriendRequestHandler extends DisruptorRequestHandler<RemoveFriendRequestPacket> {

    public static final RemoveFriendRequestHandler INSTANCE = new RemoveFriendRequestHandler();

    private RemoveFriendRequestHandler() {
    }

}