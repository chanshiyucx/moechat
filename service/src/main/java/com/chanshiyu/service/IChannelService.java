package com.chanshiyu.service;

import com.chanshiyu.mbg.entity.Channel;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @author SHIYU
 * @since 2020-11-20
 */
public interface IChannelService extends IService<Channel> {


    /**
     * 查找开放的世界频道
     */
    List<Channel> getActiveChannelList();

}
