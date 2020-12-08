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
    public List<Message> getMessageList(int toId, byte toType, int index) {
        LambdaQueryWrapper<Message> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Message::getToId, toId)
                .eq(Message::getToType, toType)
                .lt(index > 0, Message::getId, index)
                .orderByDesc(Message::getId)
                .last("limit 3");
        return messageMapper.selectList(queryWrapper);
    }

}
