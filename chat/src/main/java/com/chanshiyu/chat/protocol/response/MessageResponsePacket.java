package com.chanshiyu.chat.protocol.response;

import com.chanshiyu.chat.protocol.Packet;
import com.chanshiyu.chat.protocol.command.Command;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * @author SHIYU
 * @description 消息响应
 * @since 2020/11/9 17:29
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
public class MessageResponsePacket extends Packet {

    private int from;

    private int to;

    private byte type;

    private String avatar;

    private String message;

    private LocalDateTime createTime;

    @Override
    public Byte getCommand() {
        return Command.MESSAGE_RESPONSE;
    }

}
