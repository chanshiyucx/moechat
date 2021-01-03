package com.chanshiyu.chat.protocol.request;

import com.chanshiyu.chat.protocol.Packet;
import com.chanshiyu.chat.protocol.command.Command;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author SHIYU
 * @description
 * @since 2021/1/3 14:57
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SearchRequestPacket extends Packet {

    private String keyword;

    @Override
    public Byte getCommand() {
        return Command.SEARCH_REQUEST;
    }

}