package com.chanshiyu.chat.handler.request;

import com.chanshiyu.chat.handler.DisruptorRequestHandler;
import com.chanshiyu.chat.protocol.request.ChatMessageRequestPacket;
import io.netty.channel.ChannelHandler;

/**
 * @author SHIYU
 * @description
 * @since 2020/11/20 16:33
 */
@ChannelHandler.Sharable
public class ChatMessageRequestHandler extends DisruptorRequestHandler<ChatMessageRequestPacket> {

    public static final ChatMessageRequestHandler INSTANCE = new ChatMessageRequestHandler();

    private ChatMessageRequestHandler() {
    }

}
