package com.chanshiyu.chat.util;

import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;

/**
 * @author SHIYU
 * @description
 * @since 2020/11/10 16:16
 */
public class IDUtil {

    private static final Snowflake snowflake = IdUtil.getSnowflake(1, 1);

    public static long randomId() {
        return snowflake.nextId();
    }

}
