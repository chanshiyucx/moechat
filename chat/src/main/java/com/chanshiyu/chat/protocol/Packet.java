package com.chanshiyu.chat.protocol;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

/**
 * @author SHIYU
 * @description 抽象数据包
 * @since 2020/11/9 9:47
 */
@Data
public abstract class Packet {
    /**
     * 协议版本，通过注解使它不被序列化，避免冗余
     */
    @JSONField(deserialize = false, serialize = false)
    private byte version = 1;

    @JSONField(serialize = false)
    public abstract Byte getCommand();

}
