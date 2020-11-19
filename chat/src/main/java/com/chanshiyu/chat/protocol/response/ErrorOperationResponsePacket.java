package com.chanshiyu.chat.protocol.response;

import com.chanshiyu.chat.protocol.Packet;
import com.chanshiyu.chat.protocol.command.Command;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author SHIYU
 * @description 错误操作响应
 * @since 2020/11/16 11:26
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
public class ErrorOperationResponsePacket extends Packet {

    private boolean close;

    private String message;

    @Override
    public Byte getCommand() {
        return Command.ERROR_OPERATION_RESPONSE;
    }

}
