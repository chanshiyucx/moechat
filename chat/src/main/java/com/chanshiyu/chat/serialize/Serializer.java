package com.chanshiyu.chat.serialize;

import com.fasterxml.jackson.core.JsonProcessingException;

import java.io.IOException;

/**
 * @author SHIYU
 * @description
 * @since 2020/11/9 10:38
 */
public interface Serializer {

    /**
     * 序列化算法
     */
    byte getSerializerAlgorithm();

    /**
     * java 对象转换成二进制
     */
    byte[] serialize(Object object) throws JsonProcessingException;

    /**
     * 二进制转换成 java 对象
     */
    <T> T deserialize(Class<T> clazz, byte[] bytes) throws IOException;

}
