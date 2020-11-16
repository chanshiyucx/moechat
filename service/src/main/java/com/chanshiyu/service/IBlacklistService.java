package com.chanshiyu.service;

import com.chanshiyu.mbg.entity.Blacklist;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @author SHIYU
 * @since 2020-11-16
 */
public interface IBlacklistService extends IService<Blacklist> {

    /**
     * 根据 ip 查询是否被封禁
     */
    boolean isBlock(String ip);

}
