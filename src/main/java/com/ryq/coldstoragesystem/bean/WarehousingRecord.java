package com.ryq.coldstoragesystem.bean;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@TableName("sjz_warehousing_record")
public class WarehousingRecord {
    @TableId(value = "id",type = IdType.AUTO)
    private int id;

    @NotBlank(message = "货物名称不能为空！")
    @Size(max = 16,min = 1,message = "货物名称最长16位，最少1位")
    //货物名称
    private String itemName;
    @NotBlank(message = "货物重量不能为空！")
    @Min(value = 1,message = "货物重量不能小于0且为数字！")
    //货物重量
    private String itemWeight;
    //时间chuo
    private String registrationTimeStamp;
    //审查时间
    private String inspectRegistrationTimeStamp;
    //时间
    @TableField(exist = false)
    private String inDates;
    //时间
    @TableField(exist = false)
    private String inspectDates;
    //库房名称
    @TableField(exist = false)
    private String roomName;
    //库房编号
    private String roomCode;
    //货物标签
    private String roomLabelCode;
    //操作员
    private String operator;
    //审批员
    private String inspector;
    //状态
    private String status;
    //状态文字描述
    private String statusText;
    //备注
    private String comment;
    //编号
    private String identifier;

}
