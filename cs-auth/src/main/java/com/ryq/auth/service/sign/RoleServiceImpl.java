package com.ryq.auth.service.sign;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ryq.coldstoragesystem.bean.Role;
import com.ryq.coldstoragesystem.mapper.RoleMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {

    @Autowired
    RoleMapper roleMapper;

    @Override
    public List<Role> getAllRoleListByUsername(String userName) {
        return roleMapper.getAllRoleListByUsername(userName);
    }

    @Override
    public List<Role> getAllRoleList() {
        return roleMapper.getAllRoleList();
    }
}
