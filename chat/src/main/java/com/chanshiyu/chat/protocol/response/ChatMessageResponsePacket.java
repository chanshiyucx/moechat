package com.chanshiyu.chat.protocol.response;

import com.chanshiyu.chat.protocol.Packet;
import com.chanshiyu.chat.protocol.command.Command;
import com.chanshiyu.mbg.entity.Message;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @author SHIYU
 * @description
 * @since 2020/11/20 16:21
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ChatMessageResponsePacket extends Packet {

    List<Message> messageList;

    @Override
    public Byte getCommand() {
        return Command.CHAT_MESSAGE_RESPONSE;
    }

}