package com.chanshiyu.chat.protocol.request;

import com.chanshiyu.chat.protocol.Packet;
import com.chanshiyu.chat.protocol.command.Command;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author SHIYU
 * @description 加入群聊请求
 * @since 2020/11/10 17:42
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class JoinGroupRequestPacket extends Packet {

    private long groupId;

    @Override
    public Byte getCommand() {
        return Command.JOIN_GROUP_REQUEST;
    }

}