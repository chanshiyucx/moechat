package com.chanshiyu.chat.handler;

import com.chanshiyu.chat.handler.request.*;
import com.chanshiyu.chat.protocol.Packet;
import com.chanshiyu.chat.protocol.command.Command;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.HashMap;
import java.util.Map;

/**
 * @author SHIYU
 * @description
 * @since 2020/11/11 9:21
 */
@ChannelHandler.Sharable
public class IMHandler extends SimpleChannelInboundHandler<Packet> {

    public static final IMHandler INSTANCE = new IMHandler();

    private final Map<Byte, SimpleChannelInboundHandler<? extends Packet>> handlerMap;

    private IMHandler() {
        handlerMap = new HashMap<>();
        handlerMap.put(Command.LOGOUT_REQUEST, LogoutRequestHandler.INSTANCE);
        handlerMap.put(Command.MESSAGE_REQUEST, MessageRequestHandler.INSTANCE);
        handlerMap.put(Command.ADD_FRIEND_REQUEST, AddFriendRequestHandler.INSTANCE);
        handlerMap.put(Command.REMOVE_FRIEND_REQUEST, RemoveFriendRequestHandler.INSTANCE);
        handlerMap.put(Command.CREATE_GROUP_REQUEST, CreateGroupRequestHandler.INSTANCE);
        handlerMap.put(Command.JOIN_GROUP_REQUEST, JoinGroupRequestHandler.INSTANCE);
        handlerMap.put(Command.QUIT_GROUP_REQUEST, QuitGroupRequestHandler.INSTANCE);
        handlerMap.put(Command.CHAT_INFO_REQUEST, ChatInfoMembersRequestHandler.INSTANCE);
        handlerMap.put(Command.CHAT_MESSAGE_REQUEST, ChatMessageRequestHandler.INSTANCE);
        handlerMap.put(Command.UPDATE_USER_REQUEST, UpdateUserRequestHandler.INSTANCE);
        handlerMap.put(Command.UPDATE_GROUP_REQUEST, UpdateGroupRequestHandler.INSTANCE);
        handlerMap.put(Command.SEARCH_REQUEST, SearchRequestHandler.INSTANCE);
        handlerMap.put(Command.STATISTICS_REQUEST, StatisticsRequestHandler.INSTANCE);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Packet packet) throws Exception {
        handlerMap.get(packet.getCommand()).channelRead(ctx, packet);
    }

}
