package com.chanshiyu.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.chanshiyu.mbg.entity.Group;

/**
 * @author SHIYU
 * @since 2021-01-01
 */
public interface IGroupService extends IService<Group> {

    /**
     * 创建群组
     */
    Group create(String name, String username);

    /**
     * 查找用户创建的群组数
     */
    int findCountByCreateUser(String username);

}
