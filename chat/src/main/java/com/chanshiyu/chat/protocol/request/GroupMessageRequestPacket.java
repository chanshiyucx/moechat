package com.chanshiyu.chat.protocol.request;

import com.chanshiyu.chat.protocol.Packet;
import com.chanshiyu.chat.protocol.command.Command;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author SHIYU
 * @description 群聊消息请求
 * @since 2020/11/11 9:10
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
public class GroupMessageRequestPacket extends Packet {

    private int toGroupId;

    private String message;

    @Override
    public Byte getCommand() {
        return Command.GROUP_MESSAGE_REQUEST;
    }

}
