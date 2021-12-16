package com.ryq.common.room.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;

/**
 * 冷库仓室信息表
 * 新建一条冷库（华北石家庄冷库）仓室冷藏费用项目
 * 使用场景：系统初始化；录入新增冷库
 * 使用对象：管理员（trial：0）
 * 相关属性：仓室名称（例：仓室1），仓室编号（例：050000+001），仓室容量（例：500m³）
 * 仓室温度（例：-20℃），仓室存储标签（例：禽肉）
 */

/**
 * 报错，自定义的构造方法似乎会取消 @Data 本身的构造器方法，为了自动匹配复杂对象，需要加上无参构造器和全参构造器的注释
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName(value = "050000_room_information")
public class RoomInformation {

    @TableId(value = "id",type = IdType.AUTO)
    private Integer id;

    @NotBlank(message="库房名不能为空！")
    @Size(max = 10,min = 1,message = "库房名称最长10位，最少1位")
    private String roomName;

    private String roomCode;

    @NotBlank(message="库房可用容量不能为空！")
    @Min(value = 1,message = "库房容量不能为0且为数字！")
    private String roomVolume;

    @NotBlank(message="额定温度不能为空！")
    @Min(value = -100,message = "额定温度必须大于零下100摄氏度！")
    @Max(value = 40,message = "额定温度必须小于零上40摄氏度！")
    private String roomTemperature;

    private String roomLabelName;
    private String roomLabelCode;
    private String usedCapacity;
    private String unusedCapacity;
    private String currentTemperature;
    @NotBlank(message="每日费用不能为空！")
    @Min(value = 1,message = "日费用不能小于0且为数字！")
    private String roomDailyCost;

    private String roomKey;
    @TableField(exist = false)
    private String simpleRoomInformation;
//    private String room_name;
//    @TableId(value = "room_code")
//    private String room_code;
//    private String room_volume;
//    private String room_temperature;
//    private String room_label_name;
//    private String used_capacity;
//    private String unused_capacity;
//    private String current_temperature;
//    private String room_daily_cost;
//    private String room_key;
//    @TableField(exist = false)
//    private String simpleRoomInformation;
    @NotNull(message="库房长度不能为空！")
    @Min(value = 1,message = "库房长度不能为0且为数字！")
    @Max(value = Integer.MAX_VALUE,message = "库房长度不能超过2147483647米！")
    private Integer length;
    @NotNull(message="库房宽度不能为空！")
    @Min(value = 1,message = "库房宽度不能为0且为数字！")
    @Max(value = Integer.MAX_VALUE,message = "库房宽度不能超过2147483647米！")
    private Integer width;
    @NotNull(message="库房高度不能为空！")
    @Min(value = 1,message = "库房高度不能为0且为数字！")
    @Max(value = 10,message = "库房高度不能超过10米！")
    private Integer height;

    //经度
    private String lng;
    //纬度
    private String lat;
    //地址
    @TableField(exist = false)
    private String address;

    public RoomInformation(String roomName, String roomCode){
        this.roomName = roomName;
        this.roomCode = roomCode;
    }


}
