package com.chanshiyu.chat.protocol.response;

import com.chanshiyu.chat.protocol.Packet;
import com.chanshiyu.chat.protocol.command.Command;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author SHIYU
 * @description
 * @since 2020/11/21 10:58
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
public class MessageSuccessPacket extends Packet {

    private int id;

    private int index;

    private boolean success;

    private String message;

    @Override
    public Byte getCommand() {
        return Command.MESSAGE_SUCCESS_RESPONSE;
    }

}
