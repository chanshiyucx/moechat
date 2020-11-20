package com.chanshiyu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chanshiyu.mbg.entity.Channel;
import com.chanshiyu.mbg.mapper.ChannelMapper;
import com.chanshiyu.service.IChannelService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author SHIYU
 * @since 2020-11-20
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ChannelServiceImpl extends ServiceImpl<ChannelMapper, Channel> implements IChannelService {

    private final ChannelMapper channelMapper;

    @Override
    public List<Channel> getActiveChannelList() {
        LambdaQueryWrapper<Channel> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Channel::getStatus, 1)
                .orderByDesc(Channel::getSort);
        return channelMapper.selectList(queryWrapper);
    }

}
