package com.ryq.auth.service.sign;


import com.baomidou.mybatisplus.extension.service.IService;
import com.ryq.coldstoragesystem.bean.User;
import org.springframework.stereotype.Service;

@Service
public interface LoginService extends IService<User> {

    //获取相应用户名和密码
    public User selectByUsername(String userName);

    //根据用户名修改密码
    public Boolean updatePasswordByUserName(User user);
}
