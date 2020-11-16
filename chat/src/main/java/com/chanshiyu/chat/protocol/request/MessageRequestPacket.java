package com.chanshiyu.chat.protocol.request;

import com.chanshiyu.chat.protocol.Packet;
import com.chanshiyu.chat.protocol.command.Command;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author SHIYU
 * @description 消息请求
 * @since 2020/11/9 17:28
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
public class MessageRequestPacket extends Packet {

    private int toUserId;

    private int index;

    private String message;

    @Override
    public Byte getCommand() {
        return Command.MESSAGE_REQUEST;
    }

}
