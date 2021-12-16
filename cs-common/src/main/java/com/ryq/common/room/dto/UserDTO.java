package com.ryq.common.room.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

@Data
public class UserDTO {
    /**
     * 唯一自增主键id
     */
    @TableId(value = "id",type = IdType.AUTO)
    private Integer id;
    /**
     * 用户名
     */
    private String userName;
    /**
     * 密码
     */
    private String password;
    /**
     * 编号
     */
    private String identifier;
    /**
     * 盐
     */
    private String salt;
    /**
     * 状态
     */
    private String status;
    /**
     * 状态描述
     */
    private String statusText;
}
