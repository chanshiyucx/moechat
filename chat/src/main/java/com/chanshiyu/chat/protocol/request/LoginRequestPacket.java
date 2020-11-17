package com.chanshiyu.chat.protocol.request;

import com.chanshiyu.chat.protocol.Packet;
import com.chanshiyu.chat.protocol.command.Command;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author SHIYU
 * @description 登录请求
 * @since 2020/11/9 10:31
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
public class LoginRequestPacket extends Packet {

    private String username;

    private String password;

    private String token;

    @Override
    public Byte getCommand() {
        return Command.LOGIN_REQUEST;
    }

}
