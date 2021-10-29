package room.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class DeliveryRecord {

    @TableId(value = "id",type = IdType.AUTO)
    private int id;

    //货物名称
    @NotBlank(message = "货物名称不能为空！")
    @Size(max = 16,min = 1,message = "货物名称最长16位，最少1位")
    private String itemName;
    @NotBlank(message = "货物重量不能为空！")
    @Min(value = 1,message = "货物重量不能小于0且为数字！")
    //出库货物重量
    private String itemWeight;
    //入库时间戳
    private String inRegistrationTimeStamp;
    //出库时间戳
    private String exRegistrationTimeStamp;
    //入库时间
    @TableField(exist = false)
    private String inDate;
    //出库时间
    @TableField(exist = false)
    private String exDate;
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
    @TableField(exist = false)
    private String inspector;
    //状态
    private String status;
    //状态描述
    private String statusText;
    //编号
    private String identifier;

}
