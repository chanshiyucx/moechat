package com.chanshiyu.chat.protocol.request;

import com.chanshiyu.chat.protocol.Packet;
import com.chanshiyu.chat.protocol.command.Command;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author SHIYU
 * @description 推出群聊请求
 * @since 2020/11/10 17:44
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class QuitGroupRequestPacket extends Packet {

    private int id;

    @Override
    public Byte getCommand() {
        return Command.QUIT_GROUP_REQUEST;
    }

}