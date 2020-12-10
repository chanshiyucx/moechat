package com.chanshiyu.chat.protocol.request;

import com.chanshiyu.chat.protocol.Packet;
import com.chanshiyu.chat.protocol.command.Command;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author SHIYU
 * @description
 * @since 2020/12/10 8:52
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ChatRecentRequestPacket extends Packet {

    @Override
    public Byte getCommand() {
        return Command.CHAT_RECENT_REQUEST;
    }

}
