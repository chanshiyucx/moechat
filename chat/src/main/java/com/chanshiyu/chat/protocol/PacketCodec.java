package com.chanshiyu.chat.protocol;

import com.chanshiyu.chat.attribute.CryptoAttributes;
import com.chanshiyu.chat.protocol.command.Command;
import com.chanshiyu.chat.protocol.request.*;
import com.chanshiyu.chat.protocol.response.*;
import com.chanshiyu.chat.serialize.Serializer;
import com.chanshiyu.chat.serialize.impl.JSONSerializer;
import com.chanshiyu.common.util.CryptoAesUtil;
import io.netty.buffer.ByteBuf;

import java.util.HashMap;
import java.util.Map;

/**
 * @author SHIYU
 * @description 数据包封装
 * @since 2020/11/9 10:52
 */
public class PacketCodec {

    public static final PacketCodec INSTANCE = new PacketCodec();

    public static final int MAGIC_NUMBER = 0x12345678;

    private static final Map<Byte, Class<? extends Packet>> packetTypeMap;

    private static final Map<Byte, Serializer> serializerMap;

    static {
        packetTypeMap = new HashMap<>();
        // 心跳
        packetTypeMap.put(Command.HEARTBEAT_REQUEST, HeartBeatRequestPacket.class);
        packetTypeMap.put(Command.HEARTBEAT_RESPONSE, HeartBeatResponsePacket.class);
        // 登陆
        packetTypeMap.put(Command.LOGIN_REQUEST, LoginRequestPacket.class);
        packetTypeMap.put(Command.LOGIN_RESPONSE, LoginResponsePacket.class);
        // 登出
        packetTypeMap.put(Command.LOGOUT_REQUEST, LogoutRequestPacket.class);
        packetTypeMap.put(Command.LOGOUT_RESPONSE, LogoutResponsePacket.class);
        // 消息
        packetTypeMap.put(Command.MESSAGE_REQUEST, MessageRequestPacket.class);
        packetTypeMap.put(Command.MESSAGE_RESPONSE, MessageResponsePacket.class);
        // 创建群聊
        packetTypeMap.put(Command.CREATE_GROUP_REQUEST, CreateGroupRequestPacket.class);
        packetTypeMap.put(Command.CREATE_GROUP_RESPONSE, CreateGroupResponsePacket.class);
        // 加入群聊
        packetTypeMap.put(Command.JOIN_GROUP_REQUEST, JoinGroupRequestPacket.class);
        packetTypeMap.put(Command.JOIN_GROUP_RESPONSE, JoinGroupResponsePacket.class);
        // 退出群聊
        packetTypeMap.put(Command.QUIT_GROUP_REQUEST, QuitGroupRequestPacket.class);
        packetTypeMap.put(Command.QUIT_GROUP_RESPONSE, QuitGroupResponsePacket.class);
        // 群聊成员
        packetTypeMap.put(Command.LIST_GROUP_MEMBERS_REQUEST, ListGroupMembersRequestPacket.class);
        packetTypeMap.put(Command.LIST_GROUP_MEMBERS_RESPONSE, ListGroupMembersResponsePacket.class);
        // 聊天记录
        packetTypeMap.put(Command.CHAT_HISTORY_REQUEST, ChatHistoryRequestPacket.class);
        packetTypeMap.put(Command.CHAT_HISTORY_RESPONSE, ChatHistoryResponsePacket.class);
        // 历史消息
        packetTypeMap.put(Command.CHAT_MESSAGE_REQUEST, ChatMessageRequestPacket.class);
        packetTypeMap.put(Command.CHAT_MESSAGE_RESPONSE, ChatMessageResponsePacket.class);
        // 消息发送成功
        packetTypeMap.put(Command.MESSAGE_SUCCESS_RESPONSE, MessageSuccessPacket.class);
        // 错误响应消息
        packetTypeMap.put(Command.ERROR_OPERATION_RESPONSE, ErrorOperationResponsePacket.class);

        serializerMap = new HashMap<>();
        Serializer serializer = new JSONSerializer();
        serializerMap.put(serializer.getSerializerAlgorithm(), serializer);
    }

    public void encode(ByteBuf byteBuf, Packet packet) throws Exception {
        // 1. 序列化 java 对象
        byte[] bytes = Serializer.DEFAULT.serialize(packet);
        // 2. 数据包加密
        bytes = CryptoAesUtil.encrypt(bytes, CryptoAttributes.DEFAULT_KEY, CryptoAttributes.DEFAULT_IV);
        // 3. 实际编码过程
        byteBuf.writeInt(MAGIC_NUMBER)
                .writeByte(packet.getVersion())
                .writeByte(Serializer.DEFAULT.getSerializerAlgorithm())
                .writeByte(packet.getCommand())
                .writeInt(bytes.length)
                .writeBytes(bytes);
    }

    public Packet decode(ByteBuf byteBuf) throws Exception {
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
        // 数据包解密
        bytes = CryptoAesUtil.decrypt(bytes, CryptoAttributes.DEFAULT_KEY, CryptoAttributes.DEFAULT_IV);
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
