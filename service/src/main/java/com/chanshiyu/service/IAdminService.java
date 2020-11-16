package com.chanshiyu.service;

import com.chanshiyu.mbg.entity.Admin;
import com.baomidou.mybatisplus.extension.service.IService;
import com.chanshiyu.mbg.model.params.AdminParam;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * @author SHIYU
 * @since 2020-11-16
 */
public interface IAdminService extends IService<Admin> {

    /**
     * 根据用户名获取后台管理员
     */
    Admin getAdminByUsername(String username);

    /**
     * 注册
     */
    Admin register(AdminParam adminParam);

    /**
     * 获取用户信息
     */
    UserDetails loadUserByUsername(String username);

}
