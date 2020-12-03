package com.chanshiyu.chat.util;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.chanshiyu.chat.attribute.RedisAttributes;
import com.chanshiyu.chat.protocol.response.ErrorOperationResponsePacket;
import com.chanshiyu.common.util.SpringUtil;
import com.chanshiyu.mbg.entity.Account;
import com.chanshiyu.service.RedisService;
import io.netty.channel.Channel;

import java.util.Set;

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
                .nickname(username)
                .avatar(null)
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
    public static void addChatHistory(int userId, String chat, double time) {
        RedisService redis = getRedis();
        redis.zAdd(String.format(RedisAttributes.USER_CHAT_HISTORY, userId), chat, time);
    }

    /**
     * 获取用户聊天列表
     */
    public static Set<Object> getChatHistory(int userId) {
        RedisService redis = getRedis();
        return redis.zReverseRangeByScore(String.format(RedisAttributes.USER_CHAT_HISTORY, userId), 0, System.currentTimeMillis());
    }

    /**
     * 移除聊天成员
     */
    public static boolean removeChatHistory(int userId, String chat) {
        RedisService redis = getRedis();
        return redis.zRemove(String.format(RedisAttributes.USER_CHAT_HISTORY, userId), chat) > 0;
    }

    /**
     * 判断是否为聊天成员
     */
    public static boolean isChatMember(int userId, String chat) {
        RedisService redis = getRedis();
        return redis.zScore(String.format(RedisAttributes.USER_CHAT_HISTORY, userId), chat) != null;
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
