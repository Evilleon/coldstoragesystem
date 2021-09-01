package com.ryq.coldstoragesystem.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ryq.coldstoragesystem.bean.AddColdStorageRoomInformation;
import com.ryq.coldstoragesystem.bean.RoomCubicle;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface RoomInformationService extends IService<AddColdStorageRoomInformation> {


    // 通过编号得到仓库
    public AddColdStorageRoomInformation getRoomByRoomCode(String roomCode);

    //无条件得到仓库名称和仓库编号
    public List<AddColdStorageRoomInformation> getRoomNameAndCode();

    //无条件的到全部仓库基本信息
    public List<AddColdStorageRoomInformation> getAllRoomInformation();

    // 新增仓库信息 mybatis
//    public void addRoomInformation(AddColdStorageRoomInformation addColdStorageRoomInformation);
//    }

    //得到全部仓库编号
    public List<String> getRoomCode();

    // 新增仓库信息 mybatis-plus
    public void addRoomInformation(AddColdStorageRoomInformation addColdStorageRoomInformation);

    //根据编号且可用容量不大于0
    public List<AddColdStorageRoomInformation> getAllByRoomLabelCode(String roomLabelCode);

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
    public AddColdStorageRoomInformation getAllById(Integer id);

    //更新仓库名称、温度、当前温度、日钱
    public Boolean updateByid(AddColdStorageRoomInformation addColdStorageRoomInformation);

    //根据名称取
    public List<AddColdStorageRoomInformation> selectRoomByRoomName(String roomName);
    //根据编号取
    public List<AddColdStorageRoomInformation> selectRoomByRoomCode(String roomCode);
    //根据温度取
    public List<AddColdStorageRoomInformation> selectRoomByTemperature(String specialTemperature);
    //根据费用取
    public List<AddColdStorageRoomInformation> selectRoomByCost(String specialCost);
    //根据标签编号取
    public List<AddColdStorageRoomInformation> selectRoomByLabelCode(String roomLabelCode);

    //初始化仓库代码表
    public Boolean insertCodeTables(List<RoomCubicle> roomCubicle,String roomCode);

}
