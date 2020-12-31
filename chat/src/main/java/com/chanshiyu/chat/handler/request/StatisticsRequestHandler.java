package com.chanshiyu.chat.handler.request;

import com.chanshiyu.chat.handler.DisruptorRequestHandler;
import com.chanshiyu.chat.protocol.request.StatisticsRequestPacket;
import io.netty.channel.ChannelHandler;

/**
 * @author SHIYU
 * @description
 * @since 2020/12/31 15:29
 */
@ChannelHandler.Sharable
public class StatisticsRequestHandler extends DisruptorRequestHandler<StatisticsRequestPacket> {

    public static final StatisticsRequestHandler INSTANCE = new StatisticsRequestHandler();

    private StatisticsRequestHandler() {
    }

}