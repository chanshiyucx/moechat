package com.chanshiyu.chat.disruptor.consumer;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.chanshiyu.chat.attribute.ChannelAttributes;
import com.chanshiyu.chat.attribute.ChatTypeAttributes;
import com.chanshiyu.chat.attribute.RedisAttributes;
import com.chanshiyu.chat.disruptor.wapper.TranslatorDataWrapper;
import com.chanshiyu.chat.protocol.Packet;
import com.chanshiyu.chat.protocol.command.Command;
import com.chanshiyu.chat.protocol.request.*;
import com.chanshiyu.chat.protocol.response.*;
import com.chanshiyu.chat.session.Session;
import com.chanshiyu.chat.util.ChatUtil;
import com.chanshiyu.chat.util.SessionUtil;
import com.chanshiyu.common.util.JwtUtil;
import com.chanshiyu.common.util.SpringUtil;
import com.chanshiyu.mbg.entity.Account;
import com.chanshiyu.mbg.entity.Message;
import com.chanshiyu.mbg.model.vo.Chat;
import com.chanshiyu.mbg.model.vo.MessageVO;
import com.chanshiyu.mbg.model.vo.RecentMessage;
import com.chanshiyu.mbg.model.vo.User;
import com.chanshiyu.service.IAccountService;
import com.chanshiyu.service.IBlacklistService;
import com.chanshiyu.service.IChannelService;
import com.chanshiyu.service.IMessageService;
import com.lmax.disruptor.WorkHandler;
import io.jsonwebtoken.Claims;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

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
        // log.info("消费消息：{}", command);
        switch (command) {
            case Command.LOGIN_REQUEST:
                login(channel, (LoginRequestPacket) packet);
                break;
            case Command.LOGOUT_REQUEST:
                logout(channel);
                break;
            case Command.MESSAGE_REQUEST:
                message(ctx, (MessageRequestPacket) packet);
                break;
            case Command.ADD_FRIEND_REQUEST:
                addFriend(channel, (AddFriendRequestPacket) packet);
                break;
            case Command.REMOVE_FRIEND_REQUEST:
                removeFriend(channel, (RemoveFriendRequestPacket) packet);
                break;
            case Command.CREATE_GROUP_REQUEST:
                createGroup(channel, (CreateGroupRequestPacket) packet);
                break;
            case Command.JOIN_GROUP_REQUEST:
                joinGroup(channel, (JoinGroupRequestPacket) packet);
                break;
            case Command.QUIT_GROUP_REQUEST:
                quitGroup(channel, (QuitGroupRequestPacket) packet);
                break;
            case Command.LIST_MEMBERS_REQUEST:
                listMembers(channel, (ListMembersRequestPacket) packet);
                break;
            case Command.CHAT_MESSAGE_REQUEST:
                chatMessage(channel, (ChatMessageRequestPacket) packet);
                break;
            case Command.UPDATE_USERINFO_REQUEST:
                updateUserInfo(channel, (UpdateUserInfoRequestPacket) packet);
                break;
            default:
                log.error("command -> {} , 该消息未被处理", command);
                break;
        }
    }

    /**
     * 登录
     */
    private void login(Channel channel, LoginRequestPacket packet) {
        log.info("before count: {}", SessionUtil.getAllChannels().size());

        String ip = channel.attr(ChannelAttributes.IP).get();
        if (StringUtils.isNotBlank(ip)) {
            // ip 黑名单检测
            IBlacklistService blacklistService = SpringUtil.getBean(IBlacklistService.class);
            if (blacklistService.isBlock(ip)) {
                log.info("IP已被封禁：[{}]", ip);
                ChatUtil.sendErrorMessage(channel, true, "IP已被封禁！");
                return;
            }

            // ip 会话数检测
            if (SessionUtil.getChannelCountByIP(ip) >= 5) {
                log.info("IP连接数已达最大值：[{}]", ip);
                ChatUtil.sendErrorMessage(channel, true, "请稍后再试喵！");
                return;
            }
        }

        IAccountService accountService = SpringUtil.getBean(IAccountService.class);
        JwtUtil jwtUtil = SpringUtil.getBean(JwtUtil.class);
        String username = packet.getUsername();
        String password = packet.getPassword();
        String token = packet.getToken();
        String device = packet.getDevice();
        Account account = null;
        String errorMsg = null;
        try {
            if (StringUtils.isNotBlank(username) && StringUtils.isNotBlank(password)) {
                // 注册或登录流程
                String regex = "^[a-zA-Z0-9._-]{3,12}$";
                if (!username.matches(regex) || !password.matches(regex)) {
                    log.info("用户名或密码格式错误，username: [{}]，password: [{}]", username, password);
                    ChatUtil.sendErrorMessage(channel, false, "用户名和密码必须为3-12位数字字母下划线组合！");
                    return;
                }
                account = accountService.getAccountByUsername(username);
                if (account == null) {
                    account = accountService.register(username, password);
                    ChatUtil.setNickname(account.getId(), ChatTypeAttributes.USER, username);
                } else {
                    account = accountService.login(username, password);
                }
            } else if (StringUtils.isNotBlank(token)) {
                // token 登录，这里不需要验证过期时间
                Claims claims = jwtUtil.getClaimsFromToken(token);
                int userId = Integer.parseInt(claims.getId());
                if (!ChatUtil.isTourist(userId)) {
                    // 用户
                    account = accountService.getById(userId);
                } else {
                    // 旧游客
                    account = ChatUtil.generateTouristAccount(userId);
                }
            } else {
                // 新游客
                account = ChatUtil.generateTouristAccount();
            }
        } catch (Exception e) {
            errorMsg = e.getMessage();
        }
        if (account == null) {
            if (StringUtils.isBlank(errorMsg)) {
                errorMsg = "注册或登录失败!";
            }
            log.info("{}：[{}]", errorMsg, packet);
            ChatUtil.sendErrorMessage(channel, false, errorMsg);
            return;
        }

        // 解绑旧会话
        Channel oldChannel = SessionUtil.getChannel(account.getId());
        if (oldChannel != null) {
            log.info("用户[{}]已在别处登录，旧IP：[{}]，新IP：[{}]", account.getId(), oldChannel.attr(ChannelAttributes.IP).get(), ip);
            ChatUtil.sendErrorMessage(oldChannel, true, "该账户已在别处登录！");
            SessionUtil.unBindSession(oldChannel);
        }

        // 绑定新会话
        String nickname = ChatUtil.getNickname(account.getId(), ChatTypeAttributes.USER);
        String avatar = ChatUtil.getAvatar(account.getId(), ChatTypeAttributes.USER);
        Session session = new Session(account.getId(), account.getUsername(), nickname, avatar, ip, device, ChatUtil.isTourist(account.getId()), new Date());
        SessionUtil.bindSession(session, channel);

        // 登录成功响应
        String newToken = jwtUtil.generateToken(session.getUserId(), session.getUsername());
        LoginResponsePacket loginResponsePacket = new LoginResponsePacket(session.getUserId(), session.getUsername(), session.getNickname(), session.getAvatar(), newToken, session.isTourist(), true, "登录成功");
        channel.writeAndFlush(loginResponsePacket);
        log.info("[{}]登录成功", session.getUsername());

        // 刷新聊天列表
        refreshChatList(channel);

        log.info("after count: {}", SessionUtil.getAllChannels().size());
    }

    /**
     * 登出
     */
    private void logout(Channel channel) {
        LogoutResponsePacket logoutResponsePacket = new LogoutResponsePacket();
        logoutResponsePacket.setSuccess(true);
        channel.writeAndFlush(logoutResponsePacket);
        SessionUtil.unBindSession(channel);
    }

    /**
     * 发送消息
     */
    private void message(ChannelHandlerContext ctx, MessageRequestPacket packet) {
        IMessageService messageService = SpringUtil.getBean(IMessageService.class);
        IAccountService accountService = SpringUtil.getBean(IAccountService.class);
        Channel channel = ctx.channel();
        Session session = SessionUtil.getSession(channel);
        LocalDateTime now = LocalDateTime.now();

        // 转发消息
        ChannelGroup channelGroup = new DefaultChannelGroup(ctx.executor());
        switch (packet.getType()) {
            case ChatTypeAttributes.CHANNEL:
                SessionUtil.getAllChannels().forEach(ch -> {
                    if (ch != channel) {
                        channelGroup.add(ch);
                    }
                });
                break;
            case ChatTypeAttributes.GROUP:
                // TODO：判断是否在存在群组并且用户在群组中
                break;
            case ChatTypeAttributes.USER:
                // 判断是否存在用户
                Account account = accountService.getById(packet.getReceiver());
                if (account == null) {
                    ChatUtil.sendErrorMessage(channel, false, "该用户不存在！");
                    return;
                }
                // TODO：是否为好友
                Channel ch = SessionUtil.getChannel(packet.getReceiver());
                if (ch != null) {
                    channelGroup.add(ch);
                }
                break;
            default:
                break;
        }
        MessageResponsePacket messageResponsePacket = new MessageResponsePacket(session.getUserId(), packet.getReceiver(), packet.getType(), session.getNickname(), session.getAvatar(), packet.getMessage(), now);
        channelGroup.writeAndFlush(messageResponsePacket);

        // 保存消息
        Message message = Message.builder()
                .sender(session.getUserId())
                .username(session.getUsername())
                .receiver(packet.getReceiver())
                .type((int) packet.getType())
                .message(packet.getMessage())
                .createTime(now)
                .build();
        messageService.save(message);

        // 发送成功响应
        MessageSuccessPacket messageSuccessPacket = new MessageSuccessPacket(message.getId(), packet.getIndex(), true, "发送成功");
        channel.writeAndFlush(messageSuccessPacket);
    }

    /**
     * 添加好友
     */
    private void addFriend(Channel channel, AddFriendRequestPacket packet) {
        IAccountService accountService = SpringUtil.getBean(IAccountService.class);

        // 判断用户是否存在
        Account account = accountService.getById(packet.getUserId());
        if (account == null) {
            ChatUtil.sendErrorMessage(channel, false, "该用户不存在！");
            return;
        }

        Session session = SessionUtil.getSession(channel);
        // 判断是否为自己
        if (account.getId() == session.getUserId()) {
            ChatUtil.sendErrorMessage(channel, false, "不能添加自己为好友！");
            return;
        }

        // 判断是否已经是好友
        String chat = String.format(RedisAttributes.USER_CHAT_ITEM, packet.getUserId(), ChatTypeAttributes.USER);
        boolean isMember = ChatUtil.isChatMember(session.getUserId(), chat);
        if (isMember) {
            ChatUtil.sendErrorMessage(channel, false, "该用户已添加！");
            return;
        }

        // 判断好友数量是否已达到上线
        long size = ChatUtil.getChatHistorySize(session.getUserId());
        if (size >= 100) {
            ChatUtil.sendErrorMessage(channel, false, "好友和群组数已达上限！");
            return;
        }

        // 存入缓存
        ChatUtil.addChatHistory(session.getUserId(), chat);

        // 刷新聊天列表
        refreshChatList(channel);
    }

    /**
     * 移除好友
     */
    private void removeFriend(Channel channel, RemoveFriendRequestPacket packet) {
        Session session = SessionUtil.getSession(channel);
        String chat = String.format(RedisAttributes.USER_CHAT_ITEM, packet.getUserId(), ChatTypeAttributes.USER);
        ChatUtil.removeChatHistory(session.getUserId(), chat);
        RemoveFriendResponsePacket removeFriendResponsePacket = new RemoveFriendResponsePacket(true, "移除成功！");
        channel.writeAndFlush(removeFriendResponsePacket);
    }

    private void createGroup(Channel channel, CreateGroupRequestPacket packet) {
        String regex = "^[a-zA-Z0-9._-\\u4e00-\\u9fa5]{3,12}$";
        String name = packet.getName();
        if (!name.matches(regex)) {
            log.info("群名称格式错误，name: [{}]", name);
            ChatUtil.sendErrorMessage(channel, false, "群组名称必须为3-12位数字字母下划线中文组合！");
            return;
        }
        Session session = SessionUtil.getSession(channel);

    }

    private void joinGroup(Channel channel, JoinGroupRequestPacket packet) {
        // 1. 获取群对应的 channelGroup，然后将当前用户的 channel 添加进去
//        int groupId = packet.getGroupId();
//        ChannelGroup channelGroup = SessionUtil.getChannelGroup(groupId);
//        channelGroup.add(channel);
//        // 2. 构造加群响应发送给客户端
//        JoinGroupResponsePacket responsePacket = new JoinGroupResponsePacket();
//        responsePacket.setSuccess(true);
//        responsePacket.setGroupId(groupId);
//        channel.writeAndFlush(responsePacket);
    }

    private void quitGroup(Channel channel, QuitGroupRequestPacket packet) {
        // 1. 获取群对应的 channelGroup，然后将当前用户的 channel 移除
//        int groupId = packet.getGroupId();
//        ChannelGroup channelGroup = SessionUtil.getChannelGroup(groupId);
//        channelGroup.remove(channel);
//        // 2. 构造退群响应发送给客户端
//        QuitGroupResponsePacket responsePacket = new QuitGroupResponsePacket();
//        responsePacket.setGroupId(packet.getGroupId());
//        responsePacket.setSuccess(true);
//        channel.writeAndFlush(responsePacket);
    }

    /**
     * 频道和群组成员列表（只显示在线成员）
     */
    private void listMembers(Channel channel, ListMembersRequestPacket packet) {
        List<User> userList;
        if (packet.getType() == ChatTypeAttributes.CHANNEL) {
            // 频道
            userList = SessionUtil.getAllChannels().stream()
                    .map(SessionUtil::getSession)
                    .filter(session -> !ChatUtil.isTourist(session.getUserId()))
                    .map(session -> {
                        User user = new User();
                        BeanUtils.copyProperties(session, user);
                        return user;
                    })
                    .collect(Collectors.toList());
        } else {
            // TODO: 群组
            userList = new ArrayList<>();
        }
        ListMembersResponsePacket listMembersResponsePacket = new ListMembersResponsePacket(packet.getId(), packet.getType(), userList);
        channel.writeAndFlush(listMembersResponsePacket);
    }

    /**
     * 历史消息
     */
    private void chatMessage(Channel channel, ChatMessageRequestPacket packet) {
        List<MessageVO> messageList = getMessageList(packet.getId(), packet.getType(), packet.getIndex(), 20);
        ChatMessageResponsePacket chatMessageResponsePacket = new ChatMessageResponsePacket(packet.getId(), packet.getType(), messageList);
        channel.writeAndFlush(chatMessageResponsePacket);
    }

    /**
     * 刷新聊天列表
     */
    private void refreshChatList(Channel channel) {
        List<Chat> list = new ArrayList<>();
        // 世界频道
        IChannelService channelService = SpringUtil.getBean(IChannelService.class);
        List<Chat> globalList = channelService.getActiveChannelList().stream()
                .map(ch -> new Chat(ch.getId(), ChatTypeAttributes.CHANNEL, ch.getName(), ch.getAvatar(), ch.getCreateTime()))
                .collect(Collectors.toList());
        // 群组和好友
        Session session = SessionUtil.getSession(channel);
        List<Chat> chatList = ChatUtil.getChatHistory(session.getUserId());
        list.addAll(globalList);
        list.addAll(chatList);
        ChatHistoryResponsePacket chatHistoryResponsePacket = new ChatHistoryResponsePacket(list);
        channel.writeAndFlush(chatHistoryResponsePacket);

        // 推送最近消息
        List<RecentMessage> recentMessageList = list.stream()
                .map(chat -> {
                    List<MessageVO> messageList = getMessageList(chat.getId(), chat.getType(), 0, 10);
                    return new RecentMessage(chat.getId(), chat.getType(), messageList);
                })
                .collect(Collectors.toList());
        ChatRecentResponsePacket chatRecentResponsePacket = new ChatRecentResponsePacket(recentMessageList);
        channel.writeAndFlush(chatRecentResponsePacket);
    }

    /**
     * 查询消息列表
     */
    private List<MessageVO> getMessageList(int receiver, byte type, int index, int size) {
        IMessageService messageService = SpringUtil.getBean(IMessageService.class);
        List<Message> messageList = messageService.getMessageList(receiver, type, index, size);
        return messageList.stream()
                .map(message -> {
                    MessageVO messageVO = new MessageVO();
                    BeanUtils.copyProperties(message, messageVO);
                    messageVO.setNickname(ChatUtil.getNickname(message.getSender(), ChatTypeAttributes.USER));
                    messageVO.setAvatar(ChatUtil.getAvatar(message.getSender(), ChatTypeAttributes.USER));
                    return messageVO;
                }).collect(Collectors.toList());
    }

    /**
     * 更新用户信息
     */
    private void updateUserInfo(Channel channel, UpdateUserInfoRequestPacket packet) {
        log.info("UpdateUserInfoRequestPacket: {}", packet);
        Session session = SessionUtil.getSession(channel);
        if (session.isTourist()) {
            UpdateUserInfoResponsePacket updateUserInfoResponsePacket = new UpdateUserInfoResponsePacket(false, "游客无法更新个人信息！", null, null);
            channel.writeAndFlush(updateUserInfoResponsePacket);
            return;
        }
        int userId = session.getUserId();
        String avatar = packet.getAvatar();
        String nickname = packet.getNickname();
        String oldPassword = packet.getOldPassword();
        String newPassword = packet.getNewPassword();
        UpdateUserInfoResponsePacket updateUserInfoResponsePacket;
        try {
            if (StringUtils.isNotBlank(avatar)) {
                // 修改头像
                ChatUtil.setAvatar(userId, ChatTypeAttributes.USER, avatar);
                session.setAvatar(avatar);
            } else if (StringUtils.isNotBlank(nickname)) {
                // 修改昵称
                ChatUtil.setNickname(userId, ChatTypeAttributes.USER, nickname);
                session.setNickname(nickname);
            } else if (StringUtils.isNotBlank(oldPassword) && StringUtils.isNotBlank(newPassword)) {
                // 修改密码
                IAccountService accountService = SpringUtil.getBean(IAccountService.class);
                accountService.updatePassword(userId, oldPassword, newPassword);
            }
            updateUserInfoResponsePacket = new UpdateUserInfoResponsePacket(true, "更新成功！", session.getAvatar(), session.getNickname());
        } catch (Exception e) {
            String errorMsg = e.getMessage();
            if (StringUtils.isBlank(errorMsg)) {
                errorMsg = "更新失败！";
            }
            updateUserInfoResponsePacket = new UpdateUserInfoResponsePacket(false, errorMsg, null, null);
        }
        channel.writeAndFlush(updateUserInfoResponsePacket);
    }

}
