package com.chanshiyu.chat.protocol.response;

import com.chanshiyu.chat.protocol.Packet;
import com.chanshiyu.chat.protocol.command.Command;
import com.chanshiyu.mbg.model.vo.RecentMessage;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @author SHIYU
 * @description
 * @since 2020/12/10 8:53
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
public class ChatRecentResponsePacket extends Packet {

    List<RecentMessage> recentMessageList;

    @Override
    public Byte getCommand() {
        return Command.CHAT_RECENT_RESPONSE;
    }

}
