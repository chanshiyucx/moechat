package com.chanshiyu.chat.protocol.request;

import com.chanshiyu.chat.protocol.Packet;
import com.chanshiyu.chat.protocol.command.Command;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author SHIYU
 * @description
 * @since 2020/12/4 10:53
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class RemoveFriendRequestPacket extends Packet {

    private int userId;

    @Override
    public Byte getCommand() {
        return Command.REMOVE_FRIEND_REQUEST;
    }

}
