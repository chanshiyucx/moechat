package com.chanshiyu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chanshiyu.common.exception.BadRequestException;
import com.chanshiyu.common.util.JwtUtil;
import com.chanshiyu.mbg.entity.Admin;
import com.chanshiyu.mbg.mapper.AdminMapper;
import com.chanshiyu.mbg.model.bo.AdminUserDetails;
import com.chanshiyu.mbg.model.params.AdminParam;
import com.chanshiyu.service.IAdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * @author SHIYU
 * @since 2020-11-16
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AdminServiceImpl extends ServiceImpl<AdminMapper, Admin> implements IAdminService {

    private final AdminMapper adminMapper;

    private final JwtUtil jwtUtil;

    private final PasswordEncoder passwordEncoder;

    @Override
    public Admin getAdminByUsername(String username) {
        LambdaQueryWrapper<Admin> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Admin::getUsername, username);
        return adminMapper.selectOne(queryWrapper);
    }

    @Override
    public Admin register(AdminParam adminParam) {
        Admin admin = new Admin();
        BeanUtils.copyProperties(adminParam, admin);
        admin.setCreateTime(LocalDateTime.now());
        admin.setStatus(1);
        // 查询是否有相同用户名的用户
        Admin result = getAdminByUsername(admin.getUsername());
        if (result != null) {
            throw new BadRequestException("该用户已存在");
        }
        // 将密码进行加密操作
        String encodePassword = passwordEncoder.encode(admin.getPassword());
        admin.setPassword(encodePassword);
        adminMapper.insert(admin);
        return admin;
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        Admin admin = getAdminByUsername(username);
        if (admin == null) {
            throw new UsernameNotFoundException("用户名或密码错误");
        }
        return new AdminUserDetails(admin);
    }

}
