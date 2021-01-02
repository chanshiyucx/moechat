package com.chanshiyu.chat.handler.request;

import com.chanshiyu.chat.handler.DisruptorRequestHandler;
import com.chanshiyu.chat.protocol.request.ChatInfoRequestPacket;
import io.netty.channel.ChannelHandler;

/**
 * @author SHIYU
 * @description
 * @since 2020/11/10 17:57
 */
@ChannelHandler.Sharable
public class ChatInfoMembersRequestHandler extends DisruptorRequestHandler<ChatInfoRequestPacket> {

    public static final ChatInfoMembersRequestHandler INSTANCE = new ChatInfoMembersRequestHandler();

    private ChatInfoMembersRequestHandler() {
    }

}