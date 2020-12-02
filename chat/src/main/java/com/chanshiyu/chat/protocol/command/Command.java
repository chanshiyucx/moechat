package com.chanshiyu.chat.protocol.command;

/**
 * @author SHIYU
 * @description 指令集
 * @since 2020/11/9 10:09
 */
public interface Command {
    /**
     * 心跳
     */
    byte HEARTBEAT_REQUEST = 1;
    byte HEARTBEAT_RESPONSE = 2;

    /**
     * 登陆
     */
    byte LOGIN_REQUEST = 3;
    byte LOGIN_RESPONSE = 4;

    /**
     * 登出
     */
    byte LOGOUT_REQUEST = 5;
    byte LOGOUT_RESPONSE = 6;

    /**
     * 消息
     */
    byte MESSAGE_REQUEST = 7;
    byte MESSAGE_RESPONSE = 8;

    /**
     * 创建群聊
     */
    byte CREATE_GROUP_REQUEST = 9;
    byte CREATE_GROUP_RESPONSE = 10;

    /**
     * 加入群聊
     */
    byte JOIN_GROUP_REQUEST = 11;
    byte JOIN_GROUP_RESPONSE = 12;

    /**
     * 退出群聊
     */
    byte QUIT_GROUP_REQUEST = 13;
    byte QUIT_GROUP_RESPONSE = 14;

    /**
     * 群成员
     */
    byte LIST_GROUP_MEMBERS_REQUEST = 15;
    byte LIST_GROUP_MEMBERS_RESPONSE = 16;

    /**
     * 添加好友
     */
    byte ADD_FRIEND_REQUEST = 17;
    byte ADD_FRIEND_RESPONSE = 18;

    /**
     * 移除好友
     */
    byte REMOVE_FRIEND_REQUEST = 17;
    byte REMOVE_FRIEND_RESPONSE = 18;

    /**
     * 聊天记录
     */
    byte CHAT_HISTORY_REQUEST = 21;
    byte CHAT_HISTORY_RESPONSE = 22;

    /**
     * 历史消息
     */
    byte CHAT_MESSAGE_REQUEST = 23;
    byte CHAT_MESSAGE_RESPONSE = 24;

    /**
     * 消息发送成功
     */
    byte MESSAGE_SUCCESS_RESPONSE = 101;

    /**
     * 错误消息
     */
    byte ERROR_OPERATION_RESPONSE = 102;

}
