package com.chanshiyu.client.console;

import io.netty.channel.Channel;

import java.util.Scanner;

/**
 * @author SHIYU
 * @description
 * @since 2020/11/11 9:25
 */
public class SendToGroupConsoleCommand implements ConsoleCommand {

    @Override
    public void exec(Scanner scanner, Channel channel) {
        System.out.print("发送消息给某个群组：");
        int toGroupId = scanner.nextInt();
        String message = scanner.next();
        channel.writeAndFlush(new GroupMessageRequestPacket(toGroupId, message));
    }

}