package com.chanshiyu.chat.protocol.response;

import com.chanshiyu.chat.protocol.Packet;
import com.chanshiyu.chat.protocol.command.Command;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author SHIYU
 * @description 登录响应
 * @since 2020/11/9 15:45
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
public class LoginResponsePacket extends Packet {

    private int userId;

    private String username;

    private String nickname;

    private String token;

    private boolean success;

    private String message;

    @Override
    public Byte getCommand() {
        return Command.LOGIN_RESPONSE;
    }

}
