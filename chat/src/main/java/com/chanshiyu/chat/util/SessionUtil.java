package com.chanshiyu.chat.util;

import com.chanshiyu.chat.attribute.ChannelAttributes;
import com.chanshiyu.chat.session.Session;
import io.netty.channel.Channel;
import io.netty.channel.group.ChannelGroup;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author SHIYU
 * @description
 * @since 2020/11/9 17:38
 */
public class SessionUtil {

    private static final Map<Integer, Channel> userIdChannelMap = new ConcurrentHashMap<>();

    public static void bindSession(Session session, Channel channel) {
        if (hasLogin(channel)) {
            Session oldSession = getSession(channel);
            userIdChannelMap.remove(oldSession.getUserId());
        }
        userIdChannelMap.put(session.getUserId(), channel);
        channel.attr(ChannelAttributes.SESSION).set(session);
    }

    public static void unBindSession(Channel channel) {
        if (hasLogin(channel)) {
            Session session = getSession(channel);
            userIdChannelMap.remove(session.getUserId());
            channel.attr(ChannelAttributes.SESSION).set(null);
        }
        channel.close();
    }

    public static Session getSession(Channel channel) {
        return channel.attr(ChannelAttributes.SESSION).get();
    }

    public static boolean hasLogin(Channel channel) {
        return getSession(channel) != null;
    }

    public static Channel getChannel(int userId) {
        return userIdChannelMap.get(userId);
    }

    public static List<Channel> getAllChannels() {
        return new ArrayList<>(userIdChannelMap.values());
    }

    public static long getChannelCountByIP(String ip) {
        return userIdChannelMap.values().stream()
                .filter(channel -> channel.attr(ChannelAttributes.IP).get().equals(ip))
                .count();
    }

}
