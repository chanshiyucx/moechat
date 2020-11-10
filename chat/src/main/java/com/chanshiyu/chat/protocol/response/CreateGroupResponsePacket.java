package com.chanshiyu.chat.protocol.response;

import com.chanshiyu.chat.protocol.Packet;
import com.chanshiyu.chat.protocol.command.Command;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @author SHIYU
 * @description 创建群聊响应
 * @since 2020/11/10 15:58
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class CreateGroupResponsePacket extends Packet {

    private boolean success;

    private long groupId;

    private List<String> usernameList;

    @Override
    public Byte getCommand() {
        return Command.CREATE_GROUP_RESPONSE;
    }

}
