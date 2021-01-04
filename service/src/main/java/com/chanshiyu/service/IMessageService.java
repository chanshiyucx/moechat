package com.chanshiyu.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.chanshiyu.mbg.entity.Message;

import java.util.List;

/**
 * @author SHIYU
 * @since 2020-11-20
 */
public interface IMessageService extends IService<Message> {

    /**
     * 获取频道/群组消息列表
     */
    List<Message> getGroupMessage(int receiver, byte type, int index, int size);

    /**
     * 获取用户消息列表
     */
    List<Message> getUserMessage(int sender, int receiver, byte type, int index, int size);

}
