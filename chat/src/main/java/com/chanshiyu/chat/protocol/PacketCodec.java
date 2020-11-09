package com.chanshiyu.chat.protocol;

import com.chanshiyu.chat.protocol.command.Command;
import com.chanshiyu.chat.protocol.request.LoginRequestPacket;
import com.chanshiyu.chat.protocol.response.LoginResponsePacket;
import com.chanshiyu.chat.serialize.Serializer;
import com.chanshiyu.chat.serialize.impl.JSONSerializer;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;

import java.util.HashMap;
import java.util.Map;

/**
 * @author SHIYU
 * @description 数据包封装
 * @since 2020/11/9 10:52
 */
public class PacketCodec {

    public static final PacketCodec INSTANCE = new PacketCodec();

    private static final int MAGIC_NUMBER = 0x12345678;

    private static final Map<Byte, Class<? extends Packet>> packetTypeMap;

    private static final Map<Byte, Serializer> serializerMap;

    static {
        packetTypeMap = new HashMap<>();
        // 心跳
//        packetTypeMap.put(Command.HEARTBEAT_REQUEST, HeartBeatRequestPacket.class);
//        packetTypeMap.put(Command.HEARTBEAT_RESPONSE, HeartBeatResponsePacket.class);
        // 登陆
        packetTypeMap.put(Command.LOGIN_REQUEST, LoginRequestPacket.class);
        packetTypeMap.put(Command.LOGIN_RESPONSE, LoginResponsePacket.class);

        serializerMap = new HashMap<>();
        Serializer serializer = new JSONSerializer();
        serializerMap.put(serializer.getSerializerAlgorithm(), serializer);
    }

    public ByteBuf encode(ByteBufAllocator byteBufAllocator, Packet packet) {
        // 1. 创建 ByteBuf 对象
        ByteBuf byteBuf = byteBufAllocator.ioBuffer();
        // 2. 序列化 java 对象
        byte[] bytes = Serializer.DEFAULT.serialize(packet);
        // 3. 实际编码过程
        byteBuf.writeInt(MAGIC_NUMBER)
                .writeByte(packet.getVersion())
                .writeByte(Serializer.DEFAULT.getSerializerAlgorithm())
                .writeByte(packet.getCommand())
                .writeInt(bytes.length)
                .writeBytes(bytes);
        return byteBuf;
    }

    public Packet decode(ByteBuf byteBuf) {
        // 跳过 magic number
        byteBuf.skipBytes(4);
        // 跳过版本号
        byteBuf.skipBytes(1);
        // 序列化算法
        byte serializeAlgorithm = byteBuf.readByte();
        // 指令
        byte command = byteBuf.readByte();
        // 数据包长度
        int length = byteBuf.readInt();
        byte[] bytes = new byte[length];
        byteBuf.readBytes(bytes);
        Class<? extends Packet> requestType = getRequestType(command);
        Serializer serializer = getSerializer(serializeAlgorithm);
        if (requestType != null && serializer != null) {
            return serializer.deserialize(requestType, bytes);
        }
        return null;
    }

    private Serializer getSerializer(byte serializeAlgorithm) {
        return serializerMap.get(serializeAlgorithm);
    }

    private Class<? extends Packet> getRequestType(byte command) {
        return packetTypeMap.get(command);
    }

}
