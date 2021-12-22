package com.ryq.auth.mapper.sign;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ryq.coldstoragesystem.bean.Role;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface RoleMapper extends BaseMapper<Role> {

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
