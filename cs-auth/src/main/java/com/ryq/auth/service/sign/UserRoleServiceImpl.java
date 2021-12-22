package com.ryq.auth.service.sign;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ryq.coldstoragesystem.bean.UserRole;

import com.ryq.coldstoragesystem.mapper.UserRoleMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserRoleServiceImpl extends ServiceImpl<UserRoleMapper, UserRole> implements UserRoleService {

    @Autowired
    UserRoleMapper userRoleMapper;

    //插入
    @Override
    public Boolean insertUserRole(UserRole userRole) {
        return userRoleMapper.insertUserRole(userRole);
    }
}
