package com.chanshiyu.chat.handler;

import com.chanshiyu.chat.disruptor.RingBufferWorkerPoolFactory;
import com.chanshiyu.chat.disruptor.producer.MessageProducer;
import com.chanshiyu.chat.protocol.Packet;
import com.chanshiyu.common.util.SpringUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * @author SHIYU
 * @description
 * @since 2020/11/11 16:55
 */
public class DisruptorRequestHandler<T extends Packet> extends SimpleChannelInboundHandler<T> {

    public RingBufferWorkerPoolFactory getWorkerPoolFactory() {
        return SpringUtil.getBean(RingBufferWorkerPoolFactory.class);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, T msg) {
        MessageProducer messageProducer = getWorkerPoolFactory().getMessageProducer(msg.getCommand());
        messageProducer.publish(msg, ctx);
    }

}
