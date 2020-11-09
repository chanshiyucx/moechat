package com.chanshiyu.chat.serialize.impl;

import com.alibaba.fastjson.JSON;
import com.chanshiyu.chat.serialize.Serializer;
import com.chanshiyu.chat.serialize.SerializerAlgorithm;

/**
 * @author SHIYU
 * @description
 * @since 2020/11/9 10:44
 */
public class JSONSerializer implements Serializer {

    @Override
    public byte getSerializerAlgorithm() {
        return SerializerAlgorithm.JSON;
    }

    @Override
    public byte[] serialize(Object object) {
        return JSON.toJSONBytes(object);
    }

    @Override
    public <T> T deserialize(Class<T> clazz, byte[] bytes) {
        return JSON.parseObject(bytes, clazz);
    }

}
