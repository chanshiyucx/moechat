package com.chanshiyu.client.console;

import com.chanshiyu.chat.protocol.request.ListMembersRequestPacket;
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
        ListMembersRequestPacket listMembersRequestPacket = new ListMembersRequestPacket();
        int groupId = scanner.nextInt();
        listMembersRequestPacket.setGroupId(groupId);
        channel.writeAndFlush(listMembersRequestPacket);
    }

}