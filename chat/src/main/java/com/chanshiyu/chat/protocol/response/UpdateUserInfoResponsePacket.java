package com.chanshiyu.chat.protocol.response;

import com.chanshiyu.chat.protocol.Packet;
import com.chanshiyu.chat.protocol.command.Command;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author SHIYU
 * @description
 * @since 2020/12/28 14:40
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
public class UpdateUserInfoResponsePacket extends Packet {

    private boolean success;

    private String message;

    private String avatar;

    private String nickname;

    @Override
    public Byte getCommand() {
        return Command.UPDATE_USERINFO_RESPONSE;
    }

}