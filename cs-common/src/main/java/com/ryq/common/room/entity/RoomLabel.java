package com.ryq.common.room.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import javax.validation.constraints.*;


@Data
@TableName(value = "room_label")
public class RoomLabel {
    @TableId(value = "id",type = IdType.AUTO)
    private Integer id;
    @NotBlank(message = "标签名称不能为空！")
    @Size(max = 16,min = 1,message = "标签名称最长16位，最少1位")
    private String roomLabelName;

    @NotBlank(message = "标签编号不能为空！")
    @Size(max = 8,min = 6,message = "编号长度为7位且为数字!")
    private String roomLabelCode;

    @NotBlank(message = "单位重量不能为空！")
    @Digits(integer = 1,fraction = 2,message = "单位重量精确到整数部分一位小数部分两位!")
    private String UnitWeightFood;

    @NotBlank(message = "最低存放温度不能为空！")
    @Min(value = -100,message = "存放温度必须大于零下100摄氏度！")
    private String minTemperature;

    @NotBlank(message = "最高存放温度不能为空！")
    @Max(value = 40,message = "存放温度必须小于零上40摄氏度！")
    private String maxTemperature;
}
