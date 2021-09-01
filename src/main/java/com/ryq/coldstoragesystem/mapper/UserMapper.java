package com.ryq.coldstoragesystem.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ryq.coldstoragesystem.bean.User;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.data.relational.core.sql.In;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface UserMapper extends BaseMapper<User> {

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
