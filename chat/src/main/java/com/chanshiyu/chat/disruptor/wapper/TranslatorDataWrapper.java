package com.chanshiyu.chat.disruptor.wapper;

import com.chanshiyu.chat.protocol.Packet;
import io.netty.channel.ChannelHandlerContext;
import lombok.Data;

/**
 * @author SHIYU
 * @description
 * @since 2020/11/11 16:12
 */
@Data
public class TranslatorDataWrapper {

    private Packet packet;

    private ChannelHandlerContext ctx;

}
