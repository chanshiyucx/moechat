package com.chanshiyu.chat.disruptor.consumer;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.chanshiyu.chat.attribute.ChatAttributes;
import com.chanshiyu.chat.disruptor.wapper.TranslatorDataWrapper;
import com.chanshiyu.chat.protocol.Packet;
import com.chanshiyu.chat.protocol.command.Command;
import com.chanshiyu.chat.protocol.request.*;
import com.chanshiyu.chat.protocol.response.*;
import com.chanshiyu.chat.session.Session;
import com.chanshiyu.chat.util.SessionUtil;
import com.chanshiyu.common.util.JwtUtil;
import com.chanshiyu.common.util.SpringUtil;
import com.chanshiyu.mbg.entity.Account;
import com.chanshiyu.service.IAccountService;
import com.chanshiyu.service.IBlacklistService;
import com.lmax.disruptor.WorkHandler;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author SHIYU
 * @description
 * @since 2020/11/11 16:22
 */
@Slf4j
public class MessageConsumer implements WorkHandler<TranslatorDataWrapper> {

    @Override
    public void onEvent(TranslatorDataWrapper wrapper) {
        Packet packet = wrapper.getPacket();
        ChannelHandlerContext ctx = wrapper.getCtx();
        byte command = packet.getCommand();
        Channel channel = ctx.channel();
        log.info("消费消息：{}", command);
        switch (command) {
            case Command.LOGIN_REQUEST:
                login(channel, (LoginRequestPacket) packet);
                break;
            case Command.LOGOUT_REQUEST:
                logout(channel, (LogoutRequestPacket) packet);
                break;
            case Command.MESSAGE_REQUEST:
                message(channel, (MessageRequestPacket) packet);
                break;
            case Command.CREATE_GROUP_REQUEST:
                createGroup(ctx, (CreateGroupRequestPacket) packet);
                break;
            case Command.JOIN_GROUP_REQUEST:
                joinGroup(channel, (JoinGroupRequestPacket) packet);
                break;
            case Command.QUIT_GROUP_REQUEST:
                quitGroup(channel, (QuitGroupRequestPacket) packet);
                break;
            case Command.LIST_GROUP_MEMBERS_REQUEST:
                listGroupMembers(channel, (ListGroupMembersRequestPacket) packet);
                break;
            case Command.GROUP_MESSAGE_REQUEST:
                groupMessage(channel, (GroupMessageRequestPacket) packet);
                break;
            default:
                log.error("command -> {} , 该消息未被处理", command);
                break;
        }
    }

    private void login(Channel channel, LoginRequestPacket packet) {
        String ip = channel.attr(ChatAttributes.IP).get();
        if (StringUtils.isNotBlank(ip)) {
            // ip 黑名单检测
            IBlacklistService blacklistService = SpringUtil.getBean(IBlacklistService.class);
            if (blacklistService.isBlock(ip)) {
                log.info("IP已被封禁：[{}]", ip);
                ErrorOperationResponsePacket errorOperationResponsePacket = new ErrorOperationResponsePacket("IP已被封禁！");
                channel.writeAndFlush(errorOperationResponsePacket);
                return;
            }

            // ip 会话数检测
            if (SessionUtil.getChannelCountByIP(ip) >= 5) {
                log.info("IP连接数已达最大值：[{}]", ip);
                ErrorOperationResponsePacket errorOperationResponsePacket = new ErrorOperationResponsePacket("请稍后再试喵！");
                channel.writeAndFlush(errorOperationResponsePacket);
                return;
            }
        }

        IAccountService accountService = SpringUtil.getBean(IAccountService.class);
        JwtUtil jwtUtil = SpringUtil.getBean(JwtUtil.class);
        Account account = null;
        String errorMsg = null;
        String username = packet.getUsername();
        String password = packet.getPassword();
        String token = packet.getToken();
        try {
            if (StringUtils.isNotBlank(token)) {
                // 从 token 登录
                username = jwtUtil.getUserNameFromToken(token);
                account = accountService.getAccountByUsername(username);
            } else {
                // 注册或登录流程
                String regex = "^[a-zA-Z0-9._-]{3,12}$";
                if (!username.matches(regex) || !password.matches(regex)) {
                    log.info("用户名或密码格式错误，username: [{}]，password: [{}]", username, password);
                    ErrorOperationResponsePacket errorOperationResponsePacket = new ErrorOperationResponsePacket("用户名和密码必须为3-12位数字字母下划线组合！");
                    channel.writeAndFlush(errorOperationResponsePacket);
                    return;
                }
                account = accountService.registerOrLogin(username, password);
            }
        } catch (Exception e) {
            errorMsg = e.getMessage();
        }
        if (account == null) {
            if (StringUtils.isBlank(errorMsg)) {
                errorMsg = "注册或登录失败!";
            }
            log.info("{}：[{}]", errorMsg, packet);
            ErrorOperationResponsePacket errorOperationResponsePacket = new ErrorOperationResponsePacket(errorMsg);
            channel.writeAndFlush(errorOperationResponsePacket);
            return;
        }

        // 解绑旧会话
        Channel oldChannel = SessionUtil.getChannel(account.getId());
        if (oldChannel != null) {
            log.info("用户[{}]已在别处登录，旧IP：[{}]，新IP：[{}]", account.getId(), oldChannel.attr(ChatAttributes.IP).get(), ip);
            ErrorOperationResponsePacket errorOperationResponsePacket = new ErrorOperationResponsePacket("该账户已在别处登录！");
            oldChannel.writeAndFlush(errorOperationResponsePacket);
            SessionUtil.unBindSession(oldChannel);
        }

        // 绑定新会话
        Session session = new Session(account.getId(), account.getUsername(), account.getNickname(), ip, new Date());
        SessionUtil.bindSession(session, channel);

        // 登录成功响应
        String newToken = jwtUtil.generateToken(session.getUsername());
        LoginResponsePacket loginResponsePacket = new LoginResponsePacket(session.getUserId(), session.getUsername(), session.getNickname(), newToken, true, "登录成功");
        channel.writeAndFlush(loginResponsePacket);
        log.info("[{}]登录成功", account.getUsername());
    }

    private void logout(Channel channel, LogoutRequestPacket packet) {
        SessionUtil.unBindSession(channel);
        LogoutResponsePacket logoutResponsePacket = new LogoutResponsePacket();
        logoutResponsePacket.setSuccess(true);
        channel.writeAndFlush(logoutResponsePacket);
    }

    private void message(Channel channel, MessageRequestPacket packet) {
        // 1.拿到消息发送方的会话信息
        Session session = SessionUtil.getSession(channel);
        // 2.通过消息发送方的会话信息构造要发送的消息
        MessageResponsePacket messageResponsePacket = new MessageResponsePacket();
        messageResponsePacket.setFromUserId(session.getUserId());
        messageResponsePacket.setFromUsername(session.getUsername());
        messageResponsePacket.setMessage(packet.getMessage());
        // 3.拿到消息接收方的 channel
        Channel toUserChannel = SessionUtil.getChannel(packet.getToUserId());
        // 4.将消息发送给消息接收方
        if (toUserChannel != null && SessionUtil.hasLogin(toUserChannel)) {
            toUserChannel.writeAndFlush(messageResponsePacket);
        } else {
            log.info("[{}] 不在线，发送失败!", packet.getToUserId());
        }
    }

    private void createGroup(ChannelHandlerContext ctx, CreateGroupRequestPacket packet) {
        List<Integer> userIdList = packet.getUserIdList();
        List<String> userNameList = new ArrayList<>();
        // 1. 创建一个 channel 分组
        ChannelGroup channelGroup = new DefaultChannelGroup(ctx.executor());
        // 2. 筛选出待加入群聊的用户的 channel 和 userName
        for (int userId : userIdList) {
            Channel channel = SessionUtil.getChannel(userId);
            if (channel != null) {
                channelGroup.add(channel);
                userNameList.add(SessionUtil.getSession(channel).getUsername());
            }
        }
        // 3. 创建群聊创建结果的响应
//        long groupId = IDUtil.randomId();
//        CreateGroupResponsePacket createGroupResponsePacket = new CreateGroupResponsePacket();
//        createGroupResponsePacket.setSuccess(true);
//        createGroupResponsePacket.setGroupId(groupId);
//        createGroupResponsePacket.setUsernameList(userNameList);
//        // 4. 给每个客户端发送拉群通知
//        channelGroup.writeAndFlush(createGroupResponsePacket);
//        // 5. 保存群组相关的信息
//        SessionUtil.bindChannelGroup(groupId, channelGroup);
//        log.info("群创建成功，id：{}, 群成员：{}", createGroupResponsePacket.getGroupId(), createGroupResponsePacket.getUsernameList());
    }

    private void joinGroup(Channel channel, JoinGroupRequestPacket packet) {
        // 1. 获取群对应的 channelGroup，然后将当前用户的 channel 添加进去
        int groupId = packet.getGroupId();
        ChannelGroup channelGroup = SessionUtil.getChannelGroup(groupId);
        channelGroup.add(channel);
        // 2. 构造加群响应发送给客户端
        JoinGroupResponsePacket responsePacket = new JoinGroupResponsePacket();
        responsePacket.setSuccess(true);
        responsePacket.setGroupId(groupId);
        channel.writeAndFlush(responsePacket);
    }

    private void quitGroup(Channel channel, QuitGroupRequestPacket packet) {
        // 1. 获取群对应的 channelGroup，然后将当前用户的 channel 移除
        int groupId = packet.getGroupId();
        ChannelGroup channelGroup = SessionUtil.getChannelGroup(groupId);
        channelGroup.remove(channel);
        // 2. 构造退群响应发送给客户端
        QuitGroupResponsePacket responsePacket = new QuitGroupResponsePacket();
        responsePacket.setGroupId(packet.getGroupId());
        responsePacket.setSuccess(true);
        channel.writeAndFlush(responsePacket);
    }

    private void groupMessage(Channel channel, GroupMessageRequestPacket packet) {
        // 1.拿到 groupId 构造群聊消息的响应
        int groupId = packet.getToGroupId();
        GroupMessageResponsePacket responsePacket = new GroupMessageResponsePacket();
        responsePacket.setFromGroupId(groupId);
        responsePacket.setMessage(packet.getMessage());
        responsePacket.setFromUser(SessionUtil.getSession(channel));
        // 2. 拿到群聊对应的 channelGroup，写到每个客户端
        ChannelGroup channelGroup = SessionUtil.getChannelGroup(groupId);
        channelGroup.writeAndFlush(responsePacket);
    }

    private void listGroupMembers(Channel channel, ListGroupMembersRequestPacket packet) {
        // 1. 获取群的 ChannelGroup
        int groupId = packet.getGroupId();
        ChannelGroup channelGroup = SessionUtil.getChannelGroup(groupId);
        // 2. 遍历群成员的 channel，对应的 session，构造群成员的信息
        List<Session> sessionList = new ArrayList<>();
        for (Channel ch : channelGroup) {
            Session session = SessionUtil.getSession(ch);
            sessionList.add(session);
        }
        // 3. 构建获取成员列表响应写回到客户端
        ListGroupMembersResponsePacket responsePacket = new ListGroupMembersResponsePacket();
        responsePacket.setGroupId(groupId);
        responsePacket.setSessionList(sessionList);
        channel.writeAndFlush(responsePacket);
    }

}
