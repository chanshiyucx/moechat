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
     * 获取消息列表
     */
    List<Message> getMessageList(int receiver, byte type, int index, int size);

}
