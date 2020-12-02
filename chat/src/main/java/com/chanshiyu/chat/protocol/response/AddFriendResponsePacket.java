package com.chanshiyu.chat.protocol.response;

import com.chanshiyu.chat.protocol.Packet;
import com.chanshiyu.chat.protocol.command.Command;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author SHIYU
 * @description
 * @since 2020/11/27 17:47
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class AddFriendResponsePacket extends Packet {

    @Override
    public Byte getCommand() {
        return Command.ADD_FRIEND_RESPONSE;
    }

}
