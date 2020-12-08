package com.chanshiyu.chat.serialize.impl;

import com.chanshiyu.chat.serialize.Serializer;
import com.chanshiyu.chat.serialize.SerializerAlgorithm;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * @author SHIYU
 * @description
 * @since 2020/11/9 10:44
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class JSONSerializer implements Serializer {

    private final ObjectMapper objectMapper;

    @Override
    public byte getSerializerAlgorithm() {
        return SerializerAlgorithm.JSON;
    }

    @Override
    public byte[] serialize(Object object) throws JsonProcessingException {
        return objectMapper.writeValueAsBytes(object);
    }

    @Override
    public <T> T deserialize(Class<T> clazz, byte[] bytes) throws IOException {
        return objectMapper.readValue(bytes, clazz);
    }

}
