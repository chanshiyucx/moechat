package com.chanshiyu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chanshiyu.mbg.entity.Group;
import com.chanshiyu.mbg.mapper.GroupMapper;
import com.chanshiyu.service.IGroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author SHIYU
 * @since 2021-01-01
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class GroupServiceImpl extends ServiceImpl<GroupMapper, Group> implements IGroupService {

    private final GroupMapper groupMapper;

    @Override
    public Group create(String name, String username) {
        Group chat = Group.builder()
                .name(name)
                .createUser(username)
                .createTime(LocalDateTime.now())
                .build();
        groupMapper.insert(chat);
        return chat;
    }

    @Override
    public int findCountByCreateUser(String username) {
        LambdaQueryWrapper<Group> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Group::getCreateUser, username);
        return groupMapper.selectCount(queryWrapper);
    }

    @Override
    public List<Group> search(String keyword) {
        LambdaQueryWrapper<Group> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(Group::getName, keyword);
        return groupMapper.selectList(queryWrapper);
    }


}
