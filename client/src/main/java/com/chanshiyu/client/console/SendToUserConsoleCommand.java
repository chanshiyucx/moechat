package com.chanshiyu.client.console;

import com.chanshiyu.chat.protocol.request.MessageRequestPacket;
import io.netty.channel.Channel;

import java.util.Scanner;

/**
 * @author SHIYU
 * @description
 * @since 2020/11/10 15:45
 */
public class SendToUserConsoleCommand implements ConsoleCommand {

    @Override
    public void exec(Scanner scanner, Channel channel) {
        System.out.print("发送消息给某个用户：");
        int toUserId = scanner.nextInt();
        String message = scanner.next();
        channel.writeAndFlush(new MessageRequestPacket(toUserId, 1, message));
    }

}