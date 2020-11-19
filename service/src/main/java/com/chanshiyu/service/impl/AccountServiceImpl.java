package com.chanshiyu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chanshiyu.common.exception.AuthenticationException;
import com.chanshiyu.mbg.attribute.AccountAttributes;
import com.chanshiyu.mbg.entity.Account;
import com.chanshiyu.mbg.mapper.AccountMapper;
import com.chanshiyu.service.IAccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * @author SHIYU
 * @since 2020-11-15
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AccountServiceImpl extends ServiceImpl<AccountMapper, Account> implements IAccountService {

    private final AccountMapper accountMapper;

    private final PasswordEncoder passwordEncoder;

    @Override
    public Account getAccountByUsername(String username) {
        LambdaQueryWrapper<Account> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Account::getUsername, username);
        return accountMapper.selectOne(queryWrapper);
    }

    @Override
    public Account registerOrLogin(String username, String password) {
        Account account = getAccountByUsername(username);
        if (account == null) {
            account = Account.builder()
                    .username(username)
                    .nickname(username)
                    .password(passwordEncoder.encode(password))
                    .avatar(null)
                    .status(AccountAttributes.ACTIVE)
                    .createTime(LocalDateTime.now())
                    .build();
            accountMapper.insert(account);
        } else {
            if (!passwordEncoder.matches(password, account.getPassword())) {
                throw new BadCredentialsException("密码不正确");
            }
            if (account.getStatus() == AccountAttributes.INACTIVE) {
                throw new AuthenticationException("该账户已被封禁");
            }
        }
        return account;
    }

}
