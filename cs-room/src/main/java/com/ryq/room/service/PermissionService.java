package com.ryq.room.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ryq.coldstoragesystem.bean.Permission;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface PermissionService extends IService<Permission> {

    /**
     * 根据用户名查询所有的权限信息
     *
     * @param userName
     * @return
     */
    List<Permission> getAllPermissionListByUsername(String userName);
}
