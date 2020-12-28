package com.chanshiyu.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.chanshiyu.mbg.entity.Account;

/**
 * @author SHIYU
 * @since 2020-11-15
 */
public interface IAccountService extends IService<Account> {

    /**
     * 根据用户名获取账号
     */
    Account getAccountByUsername(String username);

    /**
     * 注册或登录
     */
    Account registerOrLogin(String username, String password);

    /**
     * 更新头像
     */
    void updateAvatar(int id, String avatar);

    /**
     * 更新昵称
     */
    void updateNickname(int id, String nickname);

    /**
     * 更新密码
     */
    void updatePassword(int id, String oldPassword, String newPassword);

}
