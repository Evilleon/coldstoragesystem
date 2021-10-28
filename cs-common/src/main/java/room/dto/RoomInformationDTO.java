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


    private Long id;

    @Valid(pattern = "^([\\s\\S]{1,10})?$", tipMsg = "roomName: 不可为空， 长度：10，类型：String")
    @ApiModelProperty
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
}
