package com.chanshiyu.chat.protocol.request;

import com.chanshiyu.chat.protocol.Packet;
import com.chanshiyu.chat.protocol.command.Command;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author SHIYU
 * @description
 * @since 2021/1/2 15:30
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class UpdateGroupRequestPacket extends Packet {

    private int id;

    private String avatar;

    private String name;

    @Override
    public Byte getCommand() {
        return Command.UPDATE_GROUP_REQUEST;
    }

}
