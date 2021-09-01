package com.ryq.coldstoragesystem.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ryq.coldstoragesystem.bean.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface LoginMapper extends BaseMapper<User> {

    //查询登录信息
    @Select("select id,user_name,password,salt,status,identifier from user where user_name=#{arg0}")
    public User selectByUsername(String userName);

    //根据用户名修改密码
    public Boolean updatePasswordByUserName(User user);
}
