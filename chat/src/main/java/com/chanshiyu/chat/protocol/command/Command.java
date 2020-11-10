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
     * 群聊
     */
    byte CREATE_GROUP_REQUEST = 9;
    byte CREATE_GROUP_RESPONSE = 10;

}
