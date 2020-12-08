package com.chanshiyu.chat.protocol;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * @author SHIYU
 * @description 抽象数据包
 * @since 2020/11/9 9:47
 */
@Data
public abstract class Packet {

    @JsonIgnore()
    private byte version = 1;

    /**
     * 仅做反序列化操作
     */
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    public abstract Byte getCommand();

}
