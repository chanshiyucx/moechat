package com.chanshiyu.chat.protocol.response;

import com.chanshiyu.chat.protocol.Packet;
import com.chanshiyu.chat.protocol.command.Command;
import com.chanshiyu.chat.session.Session;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @author SHIYU
 * @description 群成员响应
 * @since 2020/11/10 17:48
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ListGroupMembersResponsePacket extends Packet {

    private int groupId;

    private List<Session> sessionList;

    @Override
    public Byte getCommand() {
        return Command.LIST_GROUP_MEMBERS_RESPONSE;
    }

}
