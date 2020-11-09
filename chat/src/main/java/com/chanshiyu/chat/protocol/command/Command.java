package com.chanshiyu.chat.protocol.command;

/**
 * @author SHIYU
 * @description 指令集
 * @since 2020/11/9 10:09
 */
public interface Command {

    /**
     * 心跳包
     */
    byte HEARTBEAT_REQUEST = 1;
    byte HEARTBEAT_RESPONSE = 2;

    /**
     * 登陆
     */
    byte LOGIN_REQUEST = 3;
    byte LOGIN_RESPONSE = 4;

}
