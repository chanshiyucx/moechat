package com.chanshiyu.client.console;

import com.chanshiyu.chat.protocol.request.JoinGroupRequestPacket;
import io.netty.channel.Channel;

import java.util.Scanner;

/**
 * @author SHIYU
 * @description
 * @since 2020/11/10 17:40
 */
public class JoinGroupConsoleCommand implements ConsoleCommand {

    @Override
    public void exec(Scanner scanner, Channel channel) {
        System.out.print("输入 groupId，加入群聊：");
        JoinGroupRequestPacket joinGroupRequestPacket = new JoinGroupRequestPacket();
        int groupId = scanner.nextInt();
        joinGroupRequestPacket.setGroupId(groupId);
        channel.writeAndFlush(joinGroupRequestPacket);
    }

}