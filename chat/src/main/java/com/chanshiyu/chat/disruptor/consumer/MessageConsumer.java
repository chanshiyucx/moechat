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
import com.chanshiyu.chat.util.ValidUtil;
import com.chanshiyu.common.util.JwtUtil;
import com.chanshiyu.common.util.SpringUtil;
import com.chanshiyu.mbg.entity.Account;
import com.chanshiyu.mbg.entity.Group;
import com.chanshiyu.mbg.entity.Message;
import com.chanshiyu.mbg.model.vo.Chat;
import com.chanshiyu.mbg.model.vo.MessageVO;
import com.chanshiyu.mbg.model.vo.RecentMessage;
import com.chanshiyu.mbg.model.vo.User;
import com.chanshiyu.service.*;
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
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;
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
            case Command.CHAT_INFO_REQUEST:
                chatInfo(channel, (ChatInfoRequestPacket) packet);
                break;
            case Command.CHAT_MESSAGE_REQUEST:
                chatMessage(channel, (ChatMessageRequestPacket) packet);
                break;
            case Command.UPDATE_USER_REQUEST:
                updateUser(channel, (UpdateUserRequestPacket) packet);
                break;
            case Command.UPDATE_GROUP_REQUEST:
                updateGroup(channel, (UpdateGroupRequestPacket) packet);
                break;
            case Command.SEARCH_REQUEST:
                search(channel, (SearchRequestPacket) packet);
                break;
            case Command.STATISTICS_REQUEST:
                statistics(channel);
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
                if (ValidUtil.validNameOrPW(username) || ValidUtil.validNameOrPW(password)) {
                    log.info("用户名或密码格式错误，username: [{}]，password: [{}]", username, password);
                    ChatUtil.sendErrorMessage(channel, false, "用户名和密码必须为3-12位数字字母下划线组合！");
                    return;
                }
                account = accountService.getAccountByUsername(username);
                if (account == null) {
                    account = accountService.register(username, password);
                    ChatUtil.setNickname(account.getId(), ChatTypeAttributes.USER, username);
                    ChatUtil.incrRegisterUser();
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
            e.printStackTrace();
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
        refreshChatList(channel, true);
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
                // 群组
                AtomicBoolean isMember = new AtomicBoolean(false);
                ChatUtil.getGroupUser(packet.getReceiver()).forEach(bean -> {
                    int userId = (int) bean;
                    Channel ch = SessionUtil.getChannel(userId);
                    if (ch != null) {
                        if (ch == channel) {
                            isMember.set(true);
                        } else {
                            channelGroup.add(ch);
                        }
                    }
                });
                if (!isMember.get()) {
                    ChatUtil.sendErrorMessage(channel, false, "请先加入群组！");
                    return;
                }
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

        ChatUtil.incrSendMessage();
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
            ChatUtil.sendErrorMessage(channel, false, "该用户已在好友列表中！");
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

        // 刷新聊天列表【先刷新聊天列表，方便前端重置当前会话】
        refreshChatList(channel, false);

        // 成功响应
        AddFriendResponsePacket addFriendResponsePacket = new AddFriendResponsePacket(true, "添加成功", account.getId());
        channel.writeAndFlush(addFriendResponsePacket);
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
        // 刷新聊天列表
        refreshChatList(channel, false);
    }

    private void createGroup(Channel channel, CreateGroupRequestPacket packet) {
        String name = packet.getName();
        if (ValidUtil.validContent(name)) {
            ChatUtil.sendErrorMessage(channel, false, "群组名称必须为1-12位数字字母下划线中文组合！");
            return;
        }
        Session session = SessionUtil.getSession(channel);

        IGroupService groupService = SpringUtil.getBean(IGroupService.class);
        int count = groupService.findCountByCreateUser(session.getUsername());
        if (count >= 10) {
            ChatUtil.sendErrorMessage(channel, false, "创建群组数已达最大限制！");
            return;
        }

        Group group = groupService.create(name, session.getUsername());

        // 加入缓存
        ChatUtil.setNickname(group.getId(), ChatTypeAttributes.GROUP, name);
        String chat = String.format(RedisAttributes.USER_CHAT_ITEM, group.getId(), ChatTypeAttributes.GROUP);
        ChatUtil.addChatHistory(session.getUserId(), chat);
        ChatUtil.addGroupUser(group.getId(), session.getUserId());

        // 刷新聊天列表
        refreshChatList(channel, false);

        // 成功响应
        CreateGroupResponsePacket createGroupResponsePacket = new CreateGroupResponsePacket(true, "创建成功", group.getId(), group.getName());
        channel.writeAndFlush(createGroupResponsePacket);
    }

    private void joinGroup(Channel channel, JoinGroupRequestPacket packet) {
        IGroupService groupService = SpringUtil.getBean(IGroupService.class);
        int groupId = packet.getGroupId();

        // 判断群组是否存在
        Group group = groupService.getById(groupId);
        if (group == null) {
            ChatUtil.sendErrorMessage(channel, false, "该群组不存在！");
            return;
        }

        // 判断是否在群组中
        Session session = SessionUtil.getSession(channel);
        if (ChatUtil.isGroupMember(groupId, session.getUserId())) {
            ChatUtil.sendErrorMessage(channel, false, "你已在群组中！");
            return;
        }

        // 加入缓存
        String chat = String.format(RedisAttributes.USER_CHAT_ITEM, groupId, ChatTypeAttributes.GROUP);
        ChatUtil.addChatHistory(session.getUserId(), chat);
        ChatUtil.addGroupUser(groupId, session.getUserId());

        // 刷新聊天列表【先刷新聊天列表，方便前端重置当前会话】
        refreshChatList(channel, false);

        // 成功响应
        JoinGroupResponsePacket joinGroupResponsePacket = new JoinGroupResponsePacket(true, "加入成功", groupId);
        channel.writeAndFlush(joinGroupResponsePacket);
    }

    private void quitGroup(Channel channel, QuitGroupRequestPacket packet) {
        IGroupService groupService = SpringUtil.getBean(IGroupService.class);
        int groupId = packet.getGroupId();

        // 判断群组是否存在
        Group group = groupService.getById(groupId);
        if (group == null) {
            ChatUtil.sendErrorMessage(channel, false, "该群组不存在！");
            return;
        }

        // 加入缓存
        Session session = SessionUtil.getSession(channel);
        String username = session.getUsername();
        String chat = String.format(RedisAttributes.USER_CHAT_ITEM, groupId, ChatTypeAttributes.GROUP);

        if (username.equals(group.getCreateUser())) {
            // 解散群组
            ChatUtil.getGroupUser(groupId).forEach(bean -> {
                int userId = (int) bean;
                ChatUtil.removeChatHistory(userId, chat);
                // 刷新聊天列表
                Channel ch = SessionUtil.getChannel(userId);
                if (ch != null) {
                    refreshChatList(ch, false);
                }
            });
            ChatUtil.removeGroup(groupId);
            groupService.removeById(groupId);
        } else {
            // 退出群组
            ChatUtil.removeChatHistory(session.getUserId(), chat);
            ChatUtil.removeGroupUser(groupId, session.getUserId());
            // 刷新聊天列表
            refreshChatList(channel, false);
        }

        // 成功响应
        QuitGroupResponsePacket quitGroupResponsePacket = new QuitGroupResponsePacket(true, "操作成功");
        channel.writeAndFlush(quitGroupResponsePacket);
    }

    /**
     * 频道和群组成员列表（只显示在线成员）
     */
    private void chatInfo(Channel channel, ChatInfoRequestPacket packet) {
        List<User> userList;
        String createUser;
        if (packet.getType() == ChatTypeAttributes.CHANNEL) {
            // 频道
            IChannelService channelService = SpringUtil.getBean(IChannelService.class);
            createUser = channelService.getById(packet.getId()).getCreateUser();
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
            // 群组
            IGroupService groupService = SpringUtil.getBean(IGroupService.class);
            createUser = groupService.getById(packet.getId()).getCreateUser();
            userList = ChatUtil.getGroupUser(packet.getId()).stream()
                    .map(bean -> {
                        int userId = (int) bean;
                        return SessionUtil.getChannel(userId);
                    })
                    .filter(Objects::nonNull)
                    .map(ch -> {
                        Session session = SessionUtil.getSession(ch);
                        User user = new User();
                        BeanUtils.copyProperties(session, user);
                        return user;
                    })
                    .collect(Collectors.toList());
        }
        ChatInfoResponsePacket chatInfoResponsePacket = new ChatInfoResponsePacket(packet.getId(), packet.getType(), createUser, userList);
        channel.writeAndFlush(chatInfoResponsePacket);
    }

    /**
     * 历史消息
     */
    private void chatMessage(Channel channel, ChatMessageRequestPacket packet) {
        Session session = SessionUtil.getSession(channel);
        List<MessageVO> messageList = getMessageList(session.getUserId(), packet.getId(), packet.getType(), packet.getIndex(), 20);
        ChatMessageResponsePacket chatMessageResponsePacket = new ChatMessageResponsePacket(packet.getId(), packet.getType(), messageList);
        channel.writeAndFlush(chatMessageResponsePacket);
    }

    /**
     * 刷新聊天列表
     */
    private void refreshChatList(Channel channel, boolean pushMessage) {
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
        if (!pushMessage) return;
        List<RecentMessage> recentMessageList = list.stream()
                .map(chat -> {
                    List<MessageVO> messageList = getMessageList(session.getUserId(), chat.getId(), chat.getType(), 0, 10);
                    return new RecentMessage(chat.getId(), chat.getType(), messageList);
                })
                .collect(Collectors.toList());
        ChatRecentResponsePacket chatRecentResponsePacket = new ChatRecentResponsePacket(recentMessageList);
        channel.writeAndFlush(chatRecentResponsePacket);
    }

    /**
     * 查询消息列表
     */
    private List<MessageVO> getMessageList(int sender, int receiver, byte type, int index, int size) {
        IMessageService messageService = SpringUtil.getBean(IMessageService.class);
        List<Message> messageList;
        if (type == ChatTypeAttributes.USER) {
            messageList = messageService.getUserMessage(sender, receiver, type, index, size);
        } else {
            messageList = messageService.getGroupMessage(receiver, type, index, size);
        }
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
    private void updateUser(Channel channel, UpdateUserRequestPacket packet) {
        Session session = SessionUtil.getSession(channel);
        if (session.isTourist()) {
            UpdateUserResponsePacket updateUserResponsePacket = new UpdateUserResponsePacket(false, "游客无法更新个人信息！", null, null);
            channel.writeAndFlush(updateUserResponsePacket);
            return;
        }

        int userId = session.getUserId();
        String avatar = packet.getAvatar();
        String nickname = packet.getNickname();
        String oldPassword = packet.getOldPassword();
        String newPassword = packet.getNewPassword();

        IAccountService accountService = SpringUtil.getBean(IAccountService.class);
        UpdateUserResponsePacket updateUserResponsePacket;
        try {
            if (StringUtils.isNotBlank(avatar)) {
                // 修改头像
                ChatUtil.setAvatar(userId, ChatTypeAttributes.USER, avatar);
                session.setAvatar(avatar);
            } else if (StringUtils.isNotBlank(nickname)) {
                // 修改昵称
                if (ValidUtil.validContent(nickname)) {
                    ChatUtil.sendErrorMessage(channel, false, "昵称必须为1-12位数字字母下划线中文组合！");
                    return;
                }
                Account account = accountService.getById(userId);
                account.setNickname(nickname);
                accountService.updateById(account);
                ChatUtil.setNickname(userId, ChatTypeAttributes.USER, nickname);
                session.setNickname(nickname);
            } else if (StringUtils.isNotBlank(oldPassword) && StringUtils.isNotBlank(newPassword)) {
                // 修改密码
                if (ValidUtil.validNameOrPW(newPassword)) {
                    ChatUtil.sendErrorMessage(channel, false, "新密码必须为3-12位数字字母下划线组合！");
                    return;
                }
                accountService.updatePassword(userId, oldPassword, newPassword);
            }
            updateUserResponsePacket = new UpdateUserResponsePacket(true, "更新成功！", session.getAvatar(), session.getNickname());
        } catch (Exception e) {
            e.printStackTrace();
            String errorMsg = e.getMessage();
            if (StringUtils.isBlank(errorMsg)) {
                errorMsg = "更新失败！";
            }
            updateUserResponsePacket = new UpdateUserResponsePacket(false, errorMsg, null, null);
        }
        channel.writeAndFlush(updateUserResponsePacket);
    }

    /**
     * 更新群组信息
     */
    private void updateGroup(Channel channel, UpdateGroupRequestPacket packet) {
        int groupId = packet.getId();
        String avatar = packet.getAvatar();
        String name = packet.getName();

        // 判断群组是否存在
        IGroupService groupService = SpringUtil.getBean(IGroupService.class);
        Group group = groupService.getById(groupId);
        if (group == null) {
            ChatUtil.sendErrorMessage(channel, false, "该群组不存在！");
            return;
        }

        // 判断是否为群组创建者
        Session session = SessionUtil.getSession(channel);
        if (!group.getCreateUser().equals(session.getUsername())) {
            ChatUtil.sendErrorMessage(channel, false, "无权更新群组信息！");
            return;
        }

        UpdateGroupResponsePacket updateGroupResponsePacket;
        try {
            if (StringUtils.isNotBlank(avatar)) {
                // 修改头像
                ChatUtil.setAvatar(groupId, ChatTypeAttributes.GROUP, avatar);
            } else if (StringUtils.isNotBlank(name)) {
                // 修改昵称
                if (ValidUtil.validContent(name)) {
                    ChatUtil.sendErrorMessage(channel, false, "群名称必须为1-12位数字字母下划线中文组合！");
                    return;
                }
                group.setName(name);
                groupService.updateById(group);
                ChatUtil.setNickname(groupId, ChatTypeAttributes.GROUP, name);
            }
            updateGroupResponsePacket = new UpdateGroupResponsePacket(true, "更新成功！", groupId, avatar, name);
        } catch (Exception e) {
            e.printStackTrace();
            String errorMsg = e.getMessage();
            if (StringUtils.isBlank(errorMsg)) {
                errorMsg = "更新失败！";
            }
            updateGroupResponsePacket = new UpdateGroupResponsePacket(false, errorMsg, groupId, null, null);
        }
        channel.writeAndFlush(updateGroupResponsePacket);
    }

    /**
     * 搜索
     */
    private void search(Channel channel, SearchRequestPacket packet) {
        String keyword = packet.getKeyword();
        if (StringUtils.isBlank(keyword)) return;
        Session session = SessionUtil.getSession(channel);

        // 搜索群组
        IGroupService groupService = SpringUtil.getBean(IGroupService.class);
        List<Group> groupList = groupService.search(keyword);

        // 搜索用户
        IAccountService accountService = SpringUtil.getBean(IAccountService.class);
        List<Account> accountList = accountService.search(keyword);

        // 组装聊天列表
        List<Chat> chatList = new ArrayList<>();
        groupList.forEach(group -> {
            String avatar = ChatUtil.getAvatar(group.getId(), ChatTypeAttributes.GROUP);
            Chat chat = new Chat(group.getId(), ChatTypeAttributes.GROUP, group.getName(), avatar, null);
            chatList.add(chat);
        });
        accountList.forEach(account -> {
            // 用户过滤自己
            if (account.getId() == session.getUserId()) return;
            String avatar = ChatUtil.getAvatar(account.getId(), ChatTypeAttributes.USER);
            Chat chat = new Chat(account.getId(), ChatTypeAttributes.USER, account.getNickname(), avatar, null);
            chatList.add(chat);
        });

        SearchResponsePacket searchResponsePacket = new SearchResponsePacket(chatList);
        channel.writeAndFlush(searchResponsePacket);
    }

    /**
     * 统计分析
     */
    private void statistics(Channel channel) {
        final String startDate = "2021-01-01";
        long totalRegisterUser = ChatUtil.getTotalRegisterUser();
        long todayRegisterUser = ChatUtil.getTodayRegisterUser();
        long totalSendMessage = ChatUtil.getTotalSendMessage();
        long todaySendMessage = ChatUtil.getTodaySendMessage();
        StatisticsResponsePacket statisticsResponsePacket = new StatisticsResponsePacket(startDate, totalRegisterUser, todayRegisterUser, totalSendMessage, todaySendMessage);
        channel.writeAndFlush(statisticsResponsePacket);
    }

}
