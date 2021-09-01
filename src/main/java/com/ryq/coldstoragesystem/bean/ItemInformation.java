package com.ryq.coldstoragesystem.bean;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import javax.validation.constraints.*;

/**
 * 货物入库信息表
 * 使用场景：货物入库
 * 使用对象：入库记录员（trial：4）
 * 相关属性：货物名称（例：猪肉），货物总重量（例：100吨），(使用仓室（例：仓室1），使用仓室编号（例：050000 001），)
 */
@Data
public class ItemInformation {
    @TableId(value = "id")
    private Integer id;

    @NotBlank(message = "货物名称不能为空！")
    @Size(max = 16,min = 1,message = "货物名称最长16位，最少1位")
    private String itemName;//货物名称
//    private String itemUnitPrice;

    @NotBlank(message = "货物重量不能为空！")
    @Min(value = 1,message = "货物重量不能小于0且为数字！")
    private String itemWeight;//货物重量

    @TableField(exist = false)
    private RoomLabel roomLabel;
//    private String roomLabelName;
//    private String roomLabelCode;
    @TableField(exist = false)
    private AddColdStorageRoomInformation storageRoom; //仓室信息表
//    private Boolean roomKey;
    //审查时间
    private String inspectRegistrationTimeStamp;
    //入库时间
    private String registrationTimeStamp;
    //时间
    private String date;

    private String identifier;
    //操作员
    private String operator;
    //审核员
    private String inspector;
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
    private String itemVolume;
    //仓库内部位置
    private String roomPoint;
    //关联temp表的id
    private Integer tempId;
}
