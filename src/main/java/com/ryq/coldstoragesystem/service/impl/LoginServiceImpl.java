package com.ryq.coldstoragesystem.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ryq.coldstoragesystem.bean.User;
import com.ryq.coldstoragesystem.mapper.LoginMapper;
import com.ryq.coldstoragesystem.service.LoginService;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LoginServiceImpl extends ServiceImpl<LoginMapper, User> implements LoginService {

    @Autowired
    LoginMapper loginMapper;

    //根据用户名查找用户名和密码是否匹配
    @Override
    public User selectByUsername(String userName) {
        //        if (loginInformation.==0){
//            return null;
//        }
        return loginMapper.selectByUsername(userName);
    }

    //根据用户名修改密码
    @Override
    public Boolean updatePasswordByUserName(User user) {

        String password = user.getPassword();

        //加盐,16进制输出
        String salt = new SecureRandomNumberGenerator().nextBytes().toHex();

        user.setSalt(salt);

        //加密码、加盐、散列1024次
        String passwordMD5 = new Md5Hash(password,salt,1024).toHex();

        user.setPassword(passwordMD5);


        return loginMapper.updatePasswordByUserName(user);
    }


}
