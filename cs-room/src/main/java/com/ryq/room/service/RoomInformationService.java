package com.ryq.room.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ryq.common.room.entity.RoomCubicle;
import com.ryq.common.room.entity.RoomInformation;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface RoomInformationService extends IService<RoomInformation> {


    // 通过编号得到仓库
    public RoomInformation getRoomByRoomCode(String roomCode);

    //无条件得到仓库名称和仓库编号
    public List<RoomInformation> getRoomNameAndCode();

    //无条件的到全部仓库基本信息
    public List<RoomInformation> getAllRoomInformation();

    // 新增仓库信息 mybatis
//    public void addRoomInformation(AddColdStorageRoomInformation addColdStorageRoomInformation);
//    }

    //得到全部仓库编号
    public List<String> getRoomCode();

    // 新增仓库信息 mybatis-plus
    public void addRoomInformation(RoomInformation addColdStorageRoomInformation);

    //根据编号且可用容量不大于0
    public List<RoomInformation> getAllByRoomLabelCode(String roomLabelCode);

    //更新库房信息字段，包括：未使用容积、已使用容积、标签、标签代号
    //参数，5个String型，第一个是表名
    public Boolean updateVolumeAndLabel(String unusedCapacity, String usedCapacity, String labelName, String labelCode, String roomCode);

    //更新库房信息字段，包括：未使用容积、已使用容积
    //参数，3个String型
    public Boolean updateVolume(String unusedCapacity, String usedCapacity, String roomCode);

    //拷贝对应的表
    public Boolean creatTables(String tableName);

    //复制仓库代码表
    public Boolean createCodeTables(String tableName);

    //根据id查询表的编号
    public String selectById(Integer id);

    //根据id查询冷库价格
    public String selectCostById(Integer id);

    // 根据id找到仓库基本信息
    public RoomInformation getAllById(Integer id);

    //更新仓库名称、温度、当前温度、日钱
    public Boolean updateByid(RoomInformation addColdStorageRoomInformation);

    //根据名称取
    public List<RoomInformation> selectRoomByRoomName(String roomName);
    //根据编号取
    public List<RoomInformation> selectRoomByRoomCode(String roomCode);
    //根据温度取
    public List<RoomInformation> selectRoomByTemperature(String specialTemperature);
    //根据费用取
    public List<RoomInformation> selectRoomByCost(String specialCost);
    //根据标签编号取
    public List<RoomInformation> selectRoomByLabelCode(String roomLabelCode);

    //初始化仓库代码表
    public Boolean insertCodeTables(List<RoomCubicle> roomCubicle, String roomCode);

}
