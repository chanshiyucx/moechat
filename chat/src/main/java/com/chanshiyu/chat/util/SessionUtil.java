package com.chanshiyu.chat.util;

import com.chanshiyu.chat.attribute.Attributes;
import com.chanshiyu.chat.session.Session;
import io.netty.channel.Channel;
import io.netty.channel.group.ChannelGroup;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author SHIYU
 * @description
 * @since 2020/11/9 17:38
 */
public class SessionUtil {

    private static final Map<Long, Channel> userIdChannelMap = new ConcurrentHashMap<>();

    private static final Map<Long, ChannelGroup> groupIdChannelGroupMap = new ConcurrentHashMap<>();

    public static void bindSession(Session session, Channel channel) {
        userIdChannelMap.put(session.getUserId(), channel);
        channel.attr(Attributes.SESSION).set(session);
    }

    public static void unBindSession(Channel channel) {
        if (hasLogin(channel)) {
            Session session = getSession(channel);
            userIdChannelMap.remove(session.getUserId());
            channel.attr(Attributes.SESSION).set(null);
        }
    }

    public static boolean hasLogin(Channel channel) {
        return getSession(channel) != null;
    }

    public static Session getSession(Channel channel) {
        return channel.attr(Attributes.SESSION).get();
    }

    public static Channel getChannel(long userId) {
        return userIdChannelMap.get(userId);
    }

    public static void bindChannelGroup(long groupId, ChannelGroup channelGroup) {
        groupIdChannelGroupMap.put(groupId, channelGroup);
    }

    public static ChannelGroup getChannelGroup(long groupId) {
        return groupIdChannelGroupMap.get(groupId);
    }

}
