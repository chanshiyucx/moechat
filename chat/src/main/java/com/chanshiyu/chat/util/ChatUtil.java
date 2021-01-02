package com.chanshiyu.chat.util;

import cn.hutool.core.convert.Convert;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.chanshiyu.chat.attribute.RedisAttributes;
import com.chanshiyu.chat.protocol.response.ErrorOperationResponsePacket;
import com.chanshiyu.chat.session.Session;
import com.chanshiyu.common.util.SpringUtil;
import com.chanshiyu.mbg.entity.Account;
import com.chanshiyu.mbg.model.vo.Chat;
import com.chanshiyu.mbg.model.vo.User;
import com.chanshiyu.service.RedisService;
import io.netty.channel.Channel;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.ZSetOperations;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author SHIYU
 * @description
 * @since 2020/11/19 8:16
 */
public class ChatUtil {

    private static RedisService getRedis() {
        return SpringUtil.getBean(RedisService.class);
    }

    /**
     * 生成游客账户
     */
    public static Account generateTouristAccount() {
        RedisService redis = getRedis();
        long incr = redis.incr(RedisAttributes.TOURIST_ID_KEY, 1);
        int userId = (int) (incr + RedisAttributes.TOURIST_ID_START);
        return generateTouristAccount(userId);
    }

    public static Account generateTouristAccount(int userId) {
        String username = StringUtils.format(RedisAttributes.TOURIST_USERNAME, userId);
        return Account.builder()
                .id(userId)
                .username(username)
                .build();
    }

    /**
     * 判断是否为游客
     */
    public static boolean isTourist(int userId) {
        return userId > RedisAttributes.TOURIST_ID_START;
    }

    /**
     * 设置用户和群组昵称
     */
    public static void setNickname(int id, byte type, String nickname) {
        RedisService redis = getRedis();
        redis.set(String.format(RedisAttributes.NICKNAME, id, type), nickname);
    }

    /**
     * 获取用户和群组昵称
     */
    public static String getNickname(int id, byte type) {
        RedisService redis = getRedis();
        return (String) redis.get(String.format(RedisAttributes.NICKNAME, id, type));
    }

    /**
     * 设置用户和群组头像
     */
    public static void setAvatar(int id, byte type, String avatar) {
        RedisService redis = getRedis();
        redis.set(String.format(RedisAttributes.AVATAR, id, type), avatar);
    }

    /**
     * 获取用户和群组头像
     */
    public static String getAvatar(int id, byte type) {
        RedisService redis = getRedis();
        return (String) redis.get(String.format(RedisAttributes.AVATAR, id, type));
    }

    /**
     * 加入用户聊天列表
     */
    public static void addChatHistory(int userId, String chat) {
        RedisService redis = getRedis();
        redis.zAdd(String.format(RedisAttributes.USER_CHAT_HISTORY, userId), chat, System.currentTimeMillis());
    }

    /**
     * 移除聊天成员
     */
    public static void removeChatHistory(int userId, String chat) {
        RedisService redis = getRedis();
        redis.zRemove(String.format(RedisAttributes.USER_CHAT_HISTORY, userId), chat);
    }

    /**
     * 获取用户聊天列表
     */
    public static List<Chat> getChatHistory(int userId) {
        RedisService redis = getRedis();
        Set<ZSetOperations.TypedTuple<Object>> chatSet = redis.zReverseRangeWithScores(String.format(RedisAttributes.USER_CHAT_HISTORY, userId), 0, -1);
        return chatSet.stream()
                .map(bean -> {
                    String value = (String) bean.getValue();
                    Double score = bean.getScore();
                    assert value != null;
                    assert score != null;
                    String[] rest = value.split("_");
                    int id = Integer.parseInt(rest[2]);
                    byte type = Byte.parseByte(rest[3]);
                    String nickname = getNickname(id, type);
                    String avatar = getAvatar(id, type);
                    LocalDateTime time = Instant.ofEpochMilli(score.longValue()).atZone(ZoneOffset.ofHours(8)).toLocalDateTime();
                    return new Chat(id, type, nickname, avatar, time);
                })
                .collect(Collectors.toList());
    }

    /**
     * 获取用户聊天列表大小
     */
    public static long getChatHistorySize(int userId) {
        RedisService redis = getRedis();
        return redis.zSize(String.format(RedisAttributes.USER_CHAT_HISTORY, userId));
    }

    /**
     * 判断是否为聊天成员
     */
    public static boolean isChatMember(int userId, String chat) {
        RedisService redis = getRedis();
        return redis.zScore(String.format(RedisAttributes.USER_CHAT_HISTORY, userId), chat) != null;
    }

    /**
     * 添加群组成员
     */
    public static void addGroupUser(int groupId, int userId) {
        RedisService redis = getRedis();
        redis.sAdd(String.format(RedisAttributes.GROUP_USER, groupId), userId);
    }

    /**
     * 移除群组成员
     */
    public static void removeGroupUser(int groupId, int userId) {
        RedisService redis = getRedis();
        redis.sRemove(String.format(RedisAttributes.GROUP_USER, groupId), userId);
    }

    /**
     * 是否为群组成员
     */
    public static boolean isGroupMember(int groupId, int userId) {
        RedisService redis = getRedis();
        return redis.sIsMember(String.format(RedisAttributes.GROUP_USER, groupId), userId);
    }

    /**
     * 群组成员列表
     */
    public static List<User> getGroupUser(int groupId) {
        RedisService redis = getRedis();
        Set<Object> userSet = redis.sMembers(String.format(RedisAttributes.GROUP_USER, groupId));
        return userSet.stream()
                .map(bean -> {
                    int userId = (int) bean;
                    return SessionUtil.getChannel(userId);
                })
                .filter(Objects::nonNull)
                .map(channel -> {
                    Session session = SessionUtil.getSession(channel);
                    User user = new User();
                    BeanUtils.copyProperties(session, user);
                    return user;
                })
                .collect(Collectors.toList());
    }

    /**
     * 统计注册用户
     */
    public static void incrRegisterUser() {
        RedisService redis = getRedis();
        redis.incr(String.format(RedisAttributes.TODAY_REGISTER_USER, LocalDate.now().toString()), 1);
        redis.incr(RedisAttributes.TOTAL_REGISTER_USER, 1);
    }

    /**
     * 获取今日用户注册数
     */
    public static long getTodayRegisterUser() {
        RedisService redis = getRedis();
        Object value = redis.get(String.format(RedisAttributes.TODAY_REGISTER_USER, LocalDate.now().toString()));
        return Convert.toLong(value, 0L);
    }

    /**
     * 获取总用户注册数
     */
    public static long getTotalRegisterUser() {
        RedisService redis = getRedis();
        Object value = redis.get(RedisAttributes.TOTAL_REGISTER_USER);
        return Convert.toLong(value, 0L);
    }

    /**
     * 统计发送消息
     */
    public static void incrSendMessage() {
        RedisService redis = getRedis();
        redis.incr(String.format(RedisAttributes.TODAY_SEND_MESSAGE, LocalDate.now().toString()), 1);
        redis.incr(RedisAttributes.TOTAL_SEND_MESSAGE, 1);
    }

    /**
     * 获取今日发送消息
     */
    public static long getTodaySendMessage() {
        RedisService redis = getRedis();
        Object value = redis.get(String.format(RedisAttributes.TODAY_SEND_MESSAGE, LocalDate.now().toString()));
        return Convert.toLong(value, 0L);
    }

    /**
     * 获取总发送消息
     */
    public static long getTotalSendMessage() {
        RedisService redis = getRedis();
        Object value = redis.get(RedisAttributes.TOTAL_SEND_MESSAGE);
        return Convert.toLong(value, 0L);
    }

    /**
     * 发送错误操作消息
     */
    public static void sendErrorMessage(Channel channel, boolean close, String message) {
        ErrorOperationResponsePacket errorOperationResponsePacket = new ErrorOperationResponsePacket(close, message);
        channel.writeAndFlush(errorOperationResponsePacket);
    }

    /**
     * 发送消息并入库
     */
    public static void saveAndSendMessage() {

    }

}
