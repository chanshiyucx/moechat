package com.chanshiyu.service.impl;

import com.chanshiyu.mbg.entity.Account;
import com.chanshiyu.mbg.mapper.AccountMapper;
import com.chanshiyu.service.IAccountService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
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

}
