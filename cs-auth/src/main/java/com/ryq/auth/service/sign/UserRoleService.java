package com.ryq.auth.service.sign;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ryq.coldstoragesystem.bean.UserRole;
import org.springframework.stereotype.Service;

@Service
public interface UserRoleService extends IService<UserRole> {

    //插入
    Boolean insertUserRole(UserRole userRole);

}
