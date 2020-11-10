package com.chanshiyu.chat.protocol.response;

import com.chanshiyu.chat.protocol.Packet;
import com.chanshiyu.chat.protocol.command.Command;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author SHIYU
 * @description 登出响应
 * @since 2020/11/10 15:50
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class LogoutResponsePacket extends Packet {

    private boolean success;

    private String message;

    @Override
    public Byte getCommand() {
        return Command.LOGOUT_RESPONSE;
    }

}