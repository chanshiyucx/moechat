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
        CreateGroupRequestPacket createGroupRequestPacket = new CreateGroupRequestPacket();
        System.out.print("【拉人群聊】输入 userId 列表，userId 之间英文逗号隔开：");
        String userIds = scanner.next();
        List<Long> ids = Arrays.stream(userIds.split(USER_ID_SPLITTER))
                .map(s -> Long.parseLong(s.trim()))
                .collect(Collectors.toList());
        createGroupRequestPacket.setUserIdList(ids);
        channel.writeAndFlush(createGroupRequestPacket);
    }

}