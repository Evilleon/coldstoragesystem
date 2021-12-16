package com.ryq.common.room.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Transport {

    @TableId(value = "id")
    private int id;
    //卡车类型
    @NotBlank(message="卡车型号不能为空！")
    @Size(max = 30,min = 1,message = "型号最长30位，最少2位")
    private String truckType;
    //承重
    @NotBlank(message = "载重不能为空！")
    @Min(value = 1,message = "载重不能小于0且为数字！")
    private Double maxBearing;
    //租车费用（百公里费用）
    @NotBlank(message = "费用不能为空！")
    @Min(value = -1,message = "载重不能小于0且为数字！")
    private String rentalCost;

    @TableField(exist = false)
    private  String typeBearingRentalCost;

    @TableField(exist = false)
    private  String number;


}
