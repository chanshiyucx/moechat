package com.chanshiyu.service.impl;

import com.chanshiyu.mbg.entity.Message;
import com.chanshiyu.mbg.mapper.MessageMapper;
import com.chanshiyu.service.IMessageService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * @author SHIYU
 * @since 2020-11-20
 */
@Service
public class MessageServiceImpl extends ServiceImpl<MessageMapper, Message> implements IMessageService {

}
