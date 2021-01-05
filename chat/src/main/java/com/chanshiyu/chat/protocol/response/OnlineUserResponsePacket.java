package com.chanshiyu.chat.protocol.response;

import com.chanshiyu.chat.protocol.Packet;
import com.chanshiyu.chat.protocol.command.Command;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @author SHIYU
 * @description
 * @since 2021/1/5 9:38
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
public class OnlineUserResponsePacket extends Packet {

    List<Integer> onlineUser;

    @Override
    public Byte getCommand() {
        return Command.ONLINE_USER_RESPONSE;
    }

}
