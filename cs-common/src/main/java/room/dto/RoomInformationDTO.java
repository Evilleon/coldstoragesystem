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

    @Valid(pattern = "^-?[1-2][0-5]|-?[1-9]|0$", tipMsg = "roomTemperature: 不可为空， 长度：2，类型：String")
    @ApiModelProperty(name = "roomTemperature", value = "仓库温度", required = true, dataType = "String")
    private String roomTemperature;
    @Valid(pattern = "^([\\s\\S]{1,10})?$", tipMsg = "roomLabelName: 不可为空， 长度：10，类型：String")
    @ApiModelProperty(name = "roomLabelName", value = "仓库名称", required = true, dataType = "String")
    private String roomLabelName;
    @Valid(pattern = "^([\\s\\S]{1,30})?$", tipMsg = "roomLabelCode: 不可为空， 长度：30，类型：String")
    @ApiModelProperty(name = "roomLabelCode", value = "仓库标签编号", required = true, dataType = "String")
    private String roomLabelCode;
    @Valid(pattern = "^([\\s\\S]{1,10})?$", tipMsg = "usedCapacity: 不可为空， 长度：10，类型：String")
    @ApiModelProperty(name = "usedCapacity", value = "已用容量", required = true, dataType = "String")
    private String usedCapacity;
    @Valid(pattern = "^([\\s\\S]{1,10})?$", tipMsg = "unusedCapacity: 不可为空， 长度：10，类型：String")
    @ApiModelProperty(name = "unusedCapacity", value = "未用容量", required = true, dataType = "String")
    private String unusedCapacity;
    @Valid(pattern = "^([\\s\\S]{1,10})?$", tipMsg = "currentTemperature: 不可为空， 长度：10，类型：String")
    @ApiModelProperty(name = "currentTemperature", value = "当前温度", required = true, dataType = "String")
    private String currentTemperature;
    @Valid(pattern = "^([\\s\\S]{1,10})?$", tipMsg = "roomDailyCost: 不可为空， 长度：10，类型：String")
    @ApiModelProperty(name = "roomDailyCost", value = "库房日消费", required = true, dataType = "String")
    private String roomDailyCost;

    /**
     * 状态 00正常 01锁定
     */
    @Valid(pattern = "^([\\s\\S]{1,10})?$", tipMsg = "roomStatus: 不可为空， 长度：10，类型：String")
    @ApiModelProperty(name = "roomStatus", value = "库房状态", required = true, dataType = "String")
    private String roomStatus;

//    @Valid(pattern = "^([\\s\\S]{1,10})?$", tipMsg = "roomStatus: 不可为空， 长度：10，类型：String")
//    @ApiModelProperty(name = "roomStatus", value = "库房状态", required = true, dataType = "String")
//    private String simpleRoomInformation;
    @Valid(pattern = "^([\\s\\S]{1,10})?$", tipMsg = "length: 不可为空， 长度：10，类型：String")
    @ApiModelProperty(name = "length", value = "长度", required = true, dataType = "String")
    private Integer length;

    @Valid(pattern = "^([\\s\\S]{1,10})?$", tipMsg = "width: 不可为空， 长度：10，类型：String")
    @ApiModelProperty(name = "width", value = "宽度", required = true, dataType = "String")
    private Integer width;
    @Valid(pattern = "^([\\s\\S]{1,10})?$", tipMsg = "height: 不可为空， 长度：10，类型：String")
    @ApiModelProperty(name = "height", value = "高度", required = true, dataType = "String")
    private Integer height;

    @Valid(pattern = "^([\\s\\S]{1,30})?$", tipMsg = "lng: 不可为空， 长度：30，类型：String")
    @ApiModelProperty(name = "lng", value = "经度", required = true, dataType = "String")
    private String lng;
    @Valid(pattern = "^([\\s\\S]{1,10})?$", tipMsg = "lat: 不可为空， 长度：10，类型：String")
    @ApiModelProperty(name = "lat", value = "纬度", required = true, dataType = "String")
    private String lat;
    //地址
    @Valid(pattern = "^([\\s\\S]{1,60})?$", tipMsg = "address: 不可为空， 长度：60，类型：String")
    @ApiModelProperty(name = "address", value = "地址", required = true, dataType = "String")
    @TableField(exist = false)
    private String address;
}
