package com.chanshiyu.chat.protocol.request;

import com.chanshiyu.chat.protocol.Packet;
import com.chanshiyu.chat.protocol.command.Command;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author SHIYU
 * @description
 * @since 2020/12/28 14:38
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class UpdateUserRequestPacket extends Packet {

    private String avatar;

    private String nickname;

    private String oldPassword;

    private String newPassword;

    @Override
    public Byte getCommand() {
        return Command.UPDATE_USER_REQUEST;
    }

}

