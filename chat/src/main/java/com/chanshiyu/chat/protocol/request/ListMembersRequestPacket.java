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
public class ListMembersRequestPacket extends Packet {

    private int id;

    private byte type;

    @Override
    public Byte getCommand() {
        return Command.LIST_MEMBERS_REQUEST;
    }

}
