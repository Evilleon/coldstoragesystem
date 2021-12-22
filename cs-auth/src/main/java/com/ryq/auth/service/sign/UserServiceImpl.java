package com.ryq.auth.service.sign;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ryq.coldstoragesystem.bean.User;
import com.ryq.coldstoragesystem.mapper.UserMapper;
import com.ryq.coldstoragesystem.utils.DateAndTimeStamp;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    UserMapper userMapper;

    //插入用户名和密码
    @Override
    public int insertUser(User user) {

        String password = user.getPassword();

        //加盐,16进制输出
        String salt = new SecureRandomNumberGenerator().nextBytes().toHex();

        user.setSalt(salt);

        //加密码、加盐、散列1024次
        String passwordMD5 = new Md5Hash(password,salt,1024).toHex();

        user.setPassword(passwordMD5);

        int i = userMapper.insertUser(user);


        return user.getId();
    }

    //获取所有用户信息
    @Override
    public List<User> selectAllUser() {
        return userMapper.selectAllUser();
    }

    @Override
    public List<User> selectAllInspectors() {
        return userMapper.selectAllInspectors();
    }

    @Override
    public Boolean updateStatusById(User user) {
        return userMapper.updateStatusById(user);
    }

    //获取指定id用户
    @Override
    public User selectById(Integer id) {
        return userMapper.selectById(id);
    }

    //根据id删除用户
    @Override
    public Boolean deleteByIdBoolean(Integer id) {
        return userMapper.deleteByIdBoolean(id);
    }
}
