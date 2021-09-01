package com.ryq.coldstoragesystem.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ryq.coldstoragesystem.bean.Permission;
import com.ryq.coldstoragesystem.mapper.PermissionMapper;
import com.ryq.coldstoragesystem.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PermissionServiceImpl extends ServiceImpl<PermissionMapper, Permission> implements PermissionService {

    @Autowired
    PermissionMapper permissionMapper;

    @Override
    public List<Permission> getAllPermissionListByUsername(String userName) {
        return permissionMapper.getAllPermissionListByUsername(userName);
    }
}
