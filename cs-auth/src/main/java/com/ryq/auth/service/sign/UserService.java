package com.ryq.auth.service.sign;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ryq.coldstoragesystem.bean.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserService extends IService<User> {

    //插入用户名和密码
    public int insertUser(User user);

    //获取所有用户信息
    public List<User> selectAllUser();

    //获取所有审查员信息
    public List<User> selectAllInspectors();

    //更新状态
    public Boolean updateStatusById(User user);

    //获取指定id用户
    public User selectById(Integer id);

    //根据id删除用户
    public Boolean deleteByIdBoolean(Integer id);


}
