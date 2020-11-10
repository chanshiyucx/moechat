package com.chanshiyu.chat.handler.request;

import com.chanshiyu.chat.protocol.Packet;
import com.chanshiyu.chat.protocol.PacketCodec;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageCodec;

import java.util.List;

/**
 * @author SHIYU
 * @description
 * @since 2020/11/10 9:08
 */
public class SocketPacketCodecHandler extends MessageToMessageCodec<ByteBuf, Packet> {
//
//    public static final SocketPacketCodecHandler INSTANCE = new SocketPacketCodecHandler();
//
//    private SocketPacketCodecHandler() {}

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf byteBuf, List<Object> out) {
        out.add(PacketCodec.INSTANCE.decode(byteBuf));
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, Packet packet, List<Object> out) {
        ByteBuf byteBuf = ctx.channel().alloc().ioBuffer();
        PacketCodec.INSTANCE.encode(byteBuf, packet);
        out.add(byteBuf);
    }

}
