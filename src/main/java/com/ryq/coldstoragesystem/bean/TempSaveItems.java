package com.ryq.coldstoragesystem.bean;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import javax.validation.constraints.*;

/**
 * 审批相关信息
 */
@Data
@TableName("checking_item")
public class TempSaveItems {

    @TableId(value = "id",type = IdType.AUTO)
    private int id;

    @TableField(exist = false)
    private ItemInformation itemInformation;

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
    //时间
    @TableField(exist = false)
    private String dates;
    //库房名称
    @TableField(exist = false)
    private String roomName;
    //库房编号
    private String roomCode;
    //货物标签编号
    private String roomLabelCode;
    //货物标签名称
    @TableField(exist = false)
    private String roomLabelName;
    //操作员
    private String operator;
    //审批员
    private String inspector;
    //状态
    private String status;
    //备注
    private String comment;
    //编号
    private String identifier;

    @NotNull(message="货物长度不能为空！")
    @Min(value = 1,message = "货物长度不能为0且为数字！")
    @Max(value = Integer.MAX_VALUE,message = "货物长度不能超过2147483647米！")
    private Integer length;
    @NotNull(message="货物宽度不能为空！")
    @Min(value = 1,message = "货物宽度不能为0且为数字！")
    @Max(value = Integer.MAX_VALUE,message = "货物宽度不能超过2147483647米！")
    private Integer width;
    @NotNull(message="货物高度不能为空！")
    @Min(value = 1,message = "货物高度不能为0且为数字！")
    @Max(value = 10,message = "货物高度不能超过10米！")
    private Integer height;
    private String volume;

    //配送方式
    private String sendMode;
    //运输员
    private String sender;
    //运输员联系方式
    private String contact;
    //配送花费
    private String sendCost;
    //仓库内部位置
    private String roomPoint;
    //占据舱位个数
    private Integer cubicleNums;
    //货物位置
    private String lng;
    //货物位置
    private String lat;
    //冷藏费用单价
    private String saveCost;
    //冷藏费用租赁单价
    private String allCost;
    //冷藏费用租赁单价
    private String exDate;
}
