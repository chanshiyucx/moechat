package com.chanshiyu.chat.protocol.response;

import com.chanshiyu.chat.protocol.Packet;
import com.chanshiyu.chat.protocol.command.Command;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author SHIYU
 * @description
 * @since 2020/12/31 15:27
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
public class StatisticsResponsePacket extends Packet {

    private String startDate;

    private long totalRegisterUser;

    private long todayRegisterUser;

    private long totalSendMessage;

    private long todaySendMessage;

    @Override
    public Byte getCommand() {
        return Command.STATISTICS_RESPONSE;
    }

}