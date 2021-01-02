package com.chanshiyu.chat.protocol.response;

import com.chanshiyu.chat.protocol.Packet;
import com.chanshiyu.chat.protocol.command.Command;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author SHIYU
 * @description
 * @since 2021/1/2 15:31
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
public class UpdateGroupResponsePacket extends Packet {

    private boolean success;

    private String message;

    private int id;

    private String avatar;

    private String name;

    @Override
    public Byte getCommand() {
        return Command.UPDATE_GROUP_RESPONSE;
    }

}