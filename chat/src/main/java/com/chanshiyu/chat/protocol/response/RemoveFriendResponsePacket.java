package com.chanshiyu.chat.protocol.response;

import com.chanshiyu.chat.protocol.Packet;
import com.chanshiyu.chat.protocol.command.Command;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author SHIYU
 * @description
 * @since 2020/12/4 10:54
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
public class RemoveFriendResponsePacket extends Packet {

    private boolean success;

    private String message;

    @Override
    public Byte getCommand() {
        return Command.REMOVE_FRIEND_RESPONSE;
    }

}