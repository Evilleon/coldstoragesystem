package room.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import utils.Valid;

import javax.validation.constraints.*;


@Data
public class RoomInformationDTO {

    @Valid(pattern = "^(\\d{1,10})$", tipMsg = "id: 不可为空， 长度：10，类型：Long")
    @ApiModelProperty(name = "id", value = "主键列", required = true, dataType = "Long")
    private Long id;

    @Valid(pattern = "^([\\s\\S]{1,10})?$", tipMsg = "roomName: 不可为空， 长度：10，类型：String")
    @ApiModelProperty(name = "roomName", value = "仓库名称", required = true, dataType = "String")
    private String roomName;

    @Valid(pattern = "^[0-9][0-9][1-9]|[0-9][1-9][0-9]|[1-9][0-9][0-9]$", tipMsg = "roomCode: 不可为空， 长度：3，类型：String")
    @ApiModelProperty(name = "roomCode", value = "仓库编号", required = true, dataType = "String")
    private String roomCode;


    @Valid(pattern = "^\\d{1,4}$", tipMsg = "roomName: 不可为空， 长度：5，类型：String")
    @ApiModelProperty(name = "roomVolume", value = "仓库容量", required = true, dataType = "String")
    private String roomVolume;

    @Valid(pattern = "^-?[1-2][0-5]|-?[1-9]|0$", tipMsg = "roomName: 不可为空， 长度：2，类型：String")
    @ApiModelProperty(name = "roomTemperature", value = "仓库温度", required = true, dataType = "String")
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
}
