package com.chanshiyu.client.console;

import com.chanshiyu.chat.protocol.request.ChatInfoRequestPacket;
import io.netty.channel.Channel;

import java.util.Scanner;

/**
 * @author SHIYU
 * @description
 * @since 2020/11/10 17:50
 */
public class ListGroupMembersConsoleCommand implements ConsoleCommand {

    @Override
    public void exec(Scanner scanner, Channel channel) {
        System.out.print("输入 groupId，获取群成员列表：");
        ChatInfoRequestPacket chatInfoRequestPacket = new ChatInfoRequestPacket();
        int groupId = scanner.nextInt();
        chatInfoRequestPacket.setId(groupId);
        channel.writeAndFlush(chatInfoRequestPacket);
    }

}