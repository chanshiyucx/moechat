package com.chanshiyu.chat.protocol.response;

import com.chanshiyu.chat.protocol.Packet;
import com.chanshiyu.chat.protocol.command.Command;
import com.chanshiyu.chat.session.Session;
import com.chanshiyu.mbg.model.vo.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @author SHIYU
 * @description 群成员响应
 * @since 2020/11/10 17:48
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
public class ChatInfoResponsePacket extends Packet {

    private int id;

    private byte type;

    private String createUser;

    private List<User> userList;

    @Override
    public Byte getCommand() {
        return Command.CHAT_INFO_RESPONSE;
    }

}
