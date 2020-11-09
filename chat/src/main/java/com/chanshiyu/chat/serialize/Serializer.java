package com.chanshiyu.chat.serialize;

import com.chanshiyu.chat.serialize.impl.JSONSerializer;
import lombok.Data;

/**
 * @author SHIYU
 * @description
 * @since 2020/11/9 10:38
 */
public interface Serializer {

    Serializer DEFAULT = new JSONSerializer();

    /**
     * 序列化算法
     */
    byte getSerializerAlgorithm();

    /**
     * java 对象转换成二进制
     */
    byte[] serialize(Object object);

    /**
     * 二进制转换成 java 对象
     */
    <T> T deserialize(Class<T> clazz, byte[] bytes);

}
