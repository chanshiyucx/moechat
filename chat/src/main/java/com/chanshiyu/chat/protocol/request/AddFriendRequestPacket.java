package com.chanshiyu.chat.protocol.request;

import com.chanshiyu.chat.protocol.Packet;
import com.chanshiyu.chat.protocol.command.Command;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author SHIYU
 * @description
 * @since 2020/11/27 17:46
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class AddFriendRequestPacket extends Packet {

    private int userId;

    @Override
    public Byte getCommand() {
        return Command.ADD_FRIEND_REQUEST;
    }

}
