package com.chanshiyu.chat.protocol.request;

import com.chanshiyu.chat.protocol.Packet;
import com.chanshiyu.chat.protocol.command.Command;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @author SHIYU
 * @description 创建群聊请求
 * @since 2020/11/10 15:56
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class CreateGroupRequestPacket extends Packet {

    private String name;

    @Override
    public Byte getCommand() {
        return Command.CREATE_GROUP_REQUEST;
    }

}
