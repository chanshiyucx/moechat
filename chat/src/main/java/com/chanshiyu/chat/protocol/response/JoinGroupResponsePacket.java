package com.chanshiyu.chat.protocol.response;

import com.chanshiyu.chat.protocol.Packet;
import com.chanshiyu.chat.protocol.command.Command;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author SHIYU
 * @description 加入群聊响应
 * @since 2020/11/10 17:46
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
public class JoinGroupResponsePacket extends Packet {

    private boolean success;

    private String message;

    private int groupId;

    @Override
    public Byte getCommand() {
        return Command.JOIN_GROUP_RESPONSE;
    }

}