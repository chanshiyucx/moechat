package com.chanshiyu.chat.attribute;

/**
 * @author SHIYU
 * @description
 * @since 2020/11/19 8:21
 */
public interface RedisAttributes {

    // 游客 ID KEY
    String TOURIST_ID_KEY = "TOURIST_ID";

    // 游客 ID 起始值
    int TOURIST_ID_START = 20000000;

    // 游客用户名
    String TOURIST_USERNAME = "TOURIST_%d";

    // 用户和群组昵称
    String NICKNAME = "NICKNAME_%d_%d";

    // 用户和群组头像
    String AVATAR = "AVATAR_%d_%d";

    // 用户的聊天记录项目（id, 类型）
    String USER_CHAT_ITEM = "CHAT_ITEM_%d_%d";

    // 用户的聊天记录表
    String USER_CHAT_HISTORY = "CHAT_HISTORY_%d";

}
