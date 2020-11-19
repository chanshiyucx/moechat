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
    int TOURIST_ID_START = 2000000;

    // 游客用户名
    String TOURIST_USERNAME = "TOURIST_%d";

}
