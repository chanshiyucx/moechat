package com.chanshiyu.chat.protocol.response;

import com.chanshiyu.chat.protocol.Packet;
import com.chanshiyu.chat.protocol.command.Command;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author SHIYU
 * @description 加入群聊响应
 * @since 2020/11/10 17:46
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class JoinGroupResponsePacket extends Packet {

    private long groupId;

    private boolean success;

    private String message;

    @Override
    public Byte getCommand() {
        return Command.JOIN_GROUP_RESPONSE;
    }

}