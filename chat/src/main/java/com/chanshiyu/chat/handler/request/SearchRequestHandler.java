package com.chanshiyu.chat.handler.request;

import com.chanshiyu.chat.handler.DisruptorRequestHandler;
import com.chanshiyu.chat.protocol.request.SearchRequestPacket;
import io.netty.channel.ChannelHandler;

/**
 * @author SHIYU
 * @description
 * @since 2021/1/3 14:59
 */
@ChannelHandler.Sharable
public class SearchRequestHandler extends DisruptorRequestHandler<SearchRequestPacket> {

    public static final SearchRequestHandler INSTANCE = new SearchRequestHandler();

    private SearchRequestHandler() {
    }

}