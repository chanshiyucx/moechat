package com.chanshiyu.chat.protocol.request;

import com.chanshiyu.chat.protocol.Packet;
import com.chanshiyu.chat.protocol.command.Command;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author SHIYU
 * @description
 * @since 2020/11/20 16:20
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ChatMessageRequestPacket extends Packet {

    private int id;

    private byte type;

    private int index;

    @Override
    public Byte getCommand() {
        return Command.CHAT_MESSAGE_REQUEST;
    }

}
