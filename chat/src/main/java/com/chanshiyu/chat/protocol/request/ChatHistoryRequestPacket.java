package com.chanshiyu.chat.protocol.request;

import com.chanshiyu.chat.protocol.Packet;
import com.chanshiyu.chat.protocol.command.Command;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author SHIYU
 * @description
 * @since 2020/11/20 14:50
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ChatHistoryRequestPacket extends Packet {

    @Override
    public Byte getCommand() {
        return Command.CHAT_HISTORY_REQUEST;
    }

}
