package com.chanshiyu.chat.protocol.request;

import com.chanshiyu.chat.protocol.Packet;
import com.chanshiyu.chat.protocol.command.Command;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author SHIYU
 * @description
 * @since 2020/12/31 15:26
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class StatisticsRequestPacket extends Packet {

    @Override
    public Byte getCommand() {
        return Command.STATISTICS_REQUEST;
    }

}