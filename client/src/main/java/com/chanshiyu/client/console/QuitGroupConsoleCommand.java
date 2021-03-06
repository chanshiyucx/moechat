package com.chanshiyu.client.console;

import com.chanshiyu.chat.protocol.request.QuitGroupRequestPacket;
import io.netty.channel.Channel;

import java.util.Scanner;

/**
 * @author SHIYU
 * @description
 * @since 2020/11/10 17:50
 */
public class QuitGroupConsoleCommand implements ConsoleCommand {

    @Override
    public void exec(Scanner scanner, Channel channel) {
        System.out.print("输入 groupId，退出群聊：");
        QuitGroupRequestPacket quitGroupRequestPacket = new QuitGroupRequestPacket();
        int groupId = scanner.nextInt();
        quitGroupRequestPacket.setGroupId(groupId);
        channel.writeAndFlush(quitGroupRequestPacket);
    }

}