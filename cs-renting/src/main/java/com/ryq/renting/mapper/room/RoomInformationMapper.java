package com.ryq.renting.mapper.room;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ryq.coldstoragesystem.bean.AddColdStorageRoomInformation;
import com.ryq.coldstoragesystem.bean.RoomCubicle;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface RoomInformationMapper extends BaseMapper<AddColdStorageRoomInformation> {

    // 根据仓库编号找到仓库基本信息
    @Select("select * from 050000_room_information where room_code=#{roomCode}")
    public AddColdStorageRoomInformation getAllByRoomCode(String roomCode);

    // 无条件罗列仓库名称，编号
    @Select("select room_name, room_code from 050000_room_information")
    public List<AddColdStorageRoomInformation> getRoomNameAndCode();

    // 无条件罗列仓库名称，编号
    @Select("select room_code from 050000_room_information")
    public List<String> getRoomCode();

    // 无条件列举所有仓库
    //@Select("select * from 050000_room_information")
    public List<AddColdStorageRoomInformation> getAllRoomInformation();

    // 新增仓库基本信息
//    @Insert("insert into 050000_room_information values #{addColdStorageRoomInformation}")
    public void addRoomInformation(AddColdStorageRoomInformation addColdStorageRoomInformation);

    // 新增仓库基本信息
//    @Override
//    int insert(AddColdStorageRoomInformation entity);

    //根据标签编号和未使用容积不为0返回库房信息
    @Select("select * from 050000_room_information where room_label_code=#{roomLabelCode} and unused_capacity!=0")
    public List<AddColdStorageRoomInformation> getAllByRoomLabelCode(String roomLabelCode);

    //更新库房信息字段，包括：未使用容积、已使用容积、标签、标签代号
    //参数，5个String型
    @Update("update 050000_room_information set unused_capacity=#{arg0},used_capacity=#{arg1},room_label_name=#{arg2},room_label_code=#{arg3} where room_code=#{arg4}")
    public Boolean updateVolumeAndLabel(String unusedCapacity, String usedCapacity, String labelName, String labelCode, String roomCode);


    //更新库房信息字段，包括：未使用容积、已使用容积
    //参数，3个String型
    @Update("update 050000_room_information set unused_capacity=#{arg0},used_capacity=#{arg1} where room_code=#{arg2}")
    public Boolean updateVolume(String unusedCapacity, String usedCapacity, String roomCode);

    //新建仓库表
    @Update("create table ${tableName} like sjz")
    public Boolean creatTables(String tableName);


    // 根据id找到仓库基本信息
    @Select("select * from 050000_room_information where id=#{arg0}")
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
    //根据编号去

    //仓库代码表操作

    //新建仓库代码表
    @Update("create table ${tableName} like code")
    public Boolean creatCodeTables(String tableName);


    //初始化仓库代码表
    public Boolean insertCodeTables(@Param("roomCubicle") List<RoomCubicle> roomCubicle,@Param("roomCode") String roomCode);
}
