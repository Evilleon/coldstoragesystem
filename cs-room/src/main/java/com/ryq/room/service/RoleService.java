package com.ryq.room.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ryq.coldstoragesystem.bean.Role;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface RoleService extends IService<Role> {

    /**
     * 根据用户名查询所有的角色信息
     *
     * @param userName
     * @return
     */
    List<Role> getAllRoleListByUsername(String userName);

    /**
     * 查询所有角色
     */
    List<Role> getAllRoleList();

}
