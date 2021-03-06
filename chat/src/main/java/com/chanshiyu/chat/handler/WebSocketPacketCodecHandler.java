package com.chanshiyu.chat.handler;

import com.chanshiyu.chat.protocol.Packet;
import com.chanshiyu.chat.protocol.PacketCodec;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageCodec;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;

import java.util.List;

/**
 * @author SHIYU
 * @description
 * @since 2020/11/13 11:14
 */
@ChannelHandler.Sharable
public class WebSocketPacketCodecHandler extends MessageToMessageCodec<BinaryWebSocketFrame, Packet> {

    public static final WebSocketPacketCodecHandler INSTANCE = new WebSocketPacketCodecHandler();

    private WebSocketPacketCodecHandler() {}

    @Override
    protected void decode(ChannelHandlerContext ctx, BinaryWebSocketFrame msg, List<Object> out) throws Exception {
        out.add(PacketCodec.INSTANCE.decode(msg.content()));
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, Packet packet, List<Object> out) throws Exception {
        ByteBuf byteBuf = ctx.channel().alloc().ioBuffer();
        PacketCodec.INSTANCE.encode(byteBuf, packet);
        out.add(new BinaryWebSocketFrame(byteBuf));
    }

}
