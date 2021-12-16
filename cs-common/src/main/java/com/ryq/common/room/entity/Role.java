package com.ryq.common.room.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

@Data
public class Role {

    private Integer id;

    private String name;

    private String number;

    @TableField(exist = false)
    private String comment;
}
