package com.chanshiyu.chat.handler.request;

import com.chanshiyu.chat.handler.DisruptorRequestHandler;
import com.chanshiyu.chat.protocol.request.AddFriendRequestPacket;
import io.netty.channel.ChannelHandler;

/**
 * @author SHIYU
 * @description
 * @since 2020/12/4 11:00
 */
@ChannelHandler.Sharable
public class AddFriendRequestHandler extends DisruptorRequestHandler<AddFriendRequestPacket> {

    public static final AddFriendRequestHandler INSTANCE = new AddFriendRequestHandler();

    private AddFriendRequestHandler() {
    }

}