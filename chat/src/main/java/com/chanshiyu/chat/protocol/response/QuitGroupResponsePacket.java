package com.chanshiyu.chat.protocol.response;

import com.chanshiyu.chat.protocol.Packet;
import com.chanshiyu.chat.protocol.command.Command;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author SHIYU
 * @description 推出群聊响应
 * @since 2020/11/10 17:47
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
public class QuitGroupResponsePacket extends Packet {

    private boolean success;

    private String message;

    @Override
    public Byte getCommand() {
        return Command.QUIT_GROUP_RESPONSE;
    }

}