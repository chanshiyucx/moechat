package com.chanshiyu.chat.protocol.response;

import com.chanshiyu.chat.protocol.Packet;
import com.chanshiyu.chat.protocol.command.Command;
import com.chanshiyu.mbg.model.vo.Chat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @author SHIYU
 * @description
 * @since 2020/11/20 14:48
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
public class ChatHistoryResponsePacket extends Packet {

    List<Chat> chatList;

    @Override
    public Byte getCommand() {
        return Command.CHAT_HISTORY_RESPONSE;
    }

}
