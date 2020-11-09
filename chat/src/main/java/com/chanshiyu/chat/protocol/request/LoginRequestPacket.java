package com.chanshiyu.chat.protocol.request;

import com.chanshiyu.chat.protocol.Packet;
import com.chanshiyu.chat.protocol.command.Command;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author SHIYU
 * @description 登录请求
 * @since 2020/11/9 10:31
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class LoginRequestPacket extends Packet {

    private long userId;

    private String username;

    @Override
    public Byte getCommand() {
        return Command.LOGIN_REQUEST;
    }

}
