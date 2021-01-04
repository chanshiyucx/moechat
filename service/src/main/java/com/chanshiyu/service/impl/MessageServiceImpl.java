package com.chanshiyu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chanshiyu.mbg.entity.Message;
import com.chanshiyu.mbg.mapper.MessageMapper;
import com.chanshiyu.service.IMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author SHIYU
 * @since 2020-11-20
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class MessageServiceImpl extends ServiceImpl<MessageMapper, Message> implements IMessageService {

    private final MessageMapper messageMapper;

    @Override
    public List<Message> getGroupMessage(int receiver, byte type, int index, int size) {
        LambdaQueryWrapper<Message> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Message::getReceiver, receiver)
                .eq(Message::getType, type)
                .lt(index > 0, Message::getId, index)
                .orderByDesc(Message::getId)
                .last("limit " + size);
        return messageMapper.selectList(queryWrapper);
    }

    @Override
    public List<Message> getUserMessage(int sender, int receiver, byte type, int index, int size) {
        LambdaQueryWrapper<Message> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.nested(i -> i.eq(Message::getSender, sender).eq(Message::getReceiver, receiver))
                .or(i -> i.eq(Message::getSender, receiver).eq(Message::getReceiver, sender))
                .eq(Message::getType, type)
                .lt(index > 0, Message::getId, index)
                .orderByDesc(Message::getId)
                .last("limit " + size);
        return messageMapper.selectList(queryWrapper);
    }

}
