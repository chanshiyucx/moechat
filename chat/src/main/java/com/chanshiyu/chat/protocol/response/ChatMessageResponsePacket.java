package com.chanshiyu.chat.protocol.response;

import com.chanshiyu.chat.protocol.Packet;
import com.chanshiyu.chat.protocol.command.Command;
import com.chanshiyu.mbg.model.vo.MessageVO;
import lombok.AllArgsConstructor;
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
@AllArgsConstructor
public class ChatMessageResponsePacket extends Packet {

    private int id;

    private byte type;

    List<MessageVO> messageList;

    @Override
    public Byte getCommand() {
        return Command.CHAT_MESSAGE_RESPONSE;
    }

}