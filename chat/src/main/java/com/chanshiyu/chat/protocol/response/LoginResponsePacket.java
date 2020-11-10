package com.chanshiyu.chat.protocol.response;

import com.chanshiyu.chat.protocol.Packet;
import com.chanshiyu.chat.protocol.command.Command;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author SHIYU
 * @description 登录响应
 * @since 2020/11/9 15:45
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class LoginResponsePacket extends Packet {

    private long userId;

    private String username;

    private boolean success;

    private String message;

    @Override
    public Byte getCommand() {
        return Command.LOGIN_RESPONSE;
    }

}
