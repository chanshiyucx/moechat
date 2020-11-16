package com.chanshiyu.chat.protocol.request;

import com.chanshiyu.chat.protocol.Packet;
import com.chanshiyu.chat.protocol.command.Command;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author SHIYU
 * @description 群聊成员请求
 * @since 2020/11/10 17:45
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ListGroupMembersRequestPacket extends Packet {

    private int groupId;

    @Override
    public Byte getCommand() {
        return Command.LIST_GROUP_MEMBERS_REQUEST;
    }

}
