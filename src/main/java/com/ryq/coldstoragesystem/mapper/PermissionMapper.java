package com.ryq.coldstoragesystem.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ryq.coldstoragesystem.bean.Permission;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface PermissionMapper extends BaseMapper<Permission> {

    /**
     * 根据用户名查询所有的权限信息
     *
     * @param userName
     * @return
     */
    List<Permission> getAllPermissionListByUsername(String userName);
}
