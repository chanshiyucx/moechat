package com.chanshiyu.chat.attribute;

import com.chanshiyu.chat.session.Session;
import io.netty.util.AttributeKey;

/**
 * @author SHIYU
 * @description
 * @since 2020/11/9 17:39
 */
public interface ChannelAttributes {

    AttributeKey<Session> SESSION = AttributeKey.newInstance("session");

    AttributeKey<String> IP = AttributeKey.newInstance("CLIENT_IP");

}
