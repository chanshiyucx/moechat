package com.chanshiyu.client.console;

import com.chanshiyu.chat.protocol.request.CreateGroupRequestPacket;
import io.netty.channel.Channel;

import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

/**
 * @author SHIYU
 * @description
 * @since 2020/11/10 15:43
 */
public class CreateGroupConsoleCommand implements ConsoleCommand {

    private static final String USER_ID_SPLITTER = ",";

    @Override
    public void exec(Scanner scanner, Channel channel) {
        System.out.print("【拉人群聊】输入 userId 列表，userId 之间英文逗号隔开：");
        CreateGroupRequestPacket createGroupRequestPacket = new CreateGroupRequestPacket();
        String userIds = scanner.next();
        List<Integer> ids = Arrays.stream(userIds.split(USER_ID_SPLITTER))
                .map(s -> Integer.parseInt(s.trim()))
                .collect(Collectors.toList());
        createGroupRequestPacket.setUserIdList(ids);
        channel.writeAndFlush(createGroupRequestPacket);
    }

}