package com.chanshiyu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chanshiyu.mbg.entity.Blacklist;
import com.chanshiyu.mbg.mapper.BlacklistMapper;
import com.chanshiyu.service.IBlacklistService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author SHIYU
 * @since 2020-11-16
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class BlacklistServiceImpl extends ServiceImpl<BlacklistMapper, Blacklist> implements IBlacklistService {

    private final BlacklistMapper blacklistMapper;

    @Override
    public boolean isBlock(String ip) {
        LambdaQueryWrapper<Blacklist> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Blacklist::getIp, ip);
        return blacklistMapper.selectCount(queryWrapper) > 0;
    }

}
