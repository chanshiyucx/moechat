package com.chanshiyu.chat.disruptor.producer;

import com.chanshiyu.chat.disruptor.wapper.TranslatorDataWrapper;
import com.chanshiyu.chat.protocol.Packet;
import com.lmax.disruptor.RingBuffer;
import io.netty.channel.ChannelHandlerContext;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * @author SHIYU
 * @description
 * @since 2020/11/11 16:14
 */
@Slf4j
@Data
@AllArgsConstructor
public class MessageProducer {

    private Byte command;

    private RingBuffer<TranslatorDataWrapper> ringBuffer;

    public void publish(Packet packet, ChannelHandlerContext ctx) {
        log.info("发布消息：{}", packet.getCommand());
        long sequence = ringBuffer.next();
        try {
            TranslatorDataWrapper wrapper = ringBuffer.get(sequence);
            wrapper.setPacket(packet);
            wrapper.setCtx(ctx);
        } finally {
            ringBuffer.publish(sequence);
        }
    }

}