package com.ryq.coldstoragesystem.bean;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Customer {

    @TableId(value = "id")
    private int id;
    @NotBlank(message="姓名不能为空！")
    @Size(max = 10,min = 2,message = "姓名最长10位，最少2位")
    private String customerName;
    @NotBlank(message="联系方式不能为空！")
    private String customerContact;
    @NotBlank(message="联系地址不能为空！")
    private String address;
    
    private String creater;

    //经度
    private String lng;
    //纬度
    private String lat;
    //距离
    @TableField(exist = false)
    private String distance;

}
