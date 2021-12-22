package com.ryq.renting.mapper.item;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ryq.coldstoragesystem.bean.ItemInformation;
import com.ryq.coldstoragesystem.bean.RoomCubicle;
import com.ryq.coldstoragesystem.bean.TempSaveItems;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface ItemInformationMapper extends BaseMapper<ItemInformation> {

    //新增货物信息
    //参数：货物表，表名
    @Insert("insert into ${arg0}(item_name,item_weight,registration_time_stamp,operator,identifier) values(#{arg1},#{arg2},#{arg3},#{arg4},#{arg5})  ")
    @Options(useGeneratedKeys=true,keyColumn="id")
    public Boolean addItemInformtion(String tableName, String itemName, String itemWeight, String registrationTimeStamp,String operator,String identifier);


    //根据表名获得表内数据
    @Select("select * from ${arg0}")
    @Options(useGeneratedKeys=true,keyColumn="id")
    public List<ItemInformation> selectByTableName(String tableName);

    //转存入待审批表
    //@Insert("insert into checking_items(item_name,item_weight,registration_time_stamp,operator,inspector,status,room_code,comment,identifier,room_label_code) values(#{arg0},#{arg1},#{arg2},#{arg3},#{arg4},#{arg5},#{arg6},#{arg7},#{arg8},#{arg9})")
    public Boolean addCheckitem(TempSaveItems tempSaveItems);

    //根据表名入库
    //@Insert("insert into #{arg1}(item_name,item_weight,inspect_registration_time_stamp,registration_time_stamp,identifier,operator,inspector) values(#{itemName},#{itemWeight},#{inspectRegistrationTimeStamp},#{registrationTimeStamp},#{identifier},#{operator},#{inspector})")
    public Boolean insertByRoomCode(@Param("item") ItemInformation itemInformation, @Param("roomCode") String roomCode);

    //根据id得到相应数据
    @Select("select * from ${arg0} where id=#{arg1}")
    public ItemInformation selectById(String roomCode,Integer id);

    //根据库名和id删除相应数据
    public Boolean deleteByIdCustom(String roomCode,String id);

    //根据库名和id修改重量字段
    public Boolean updateById(String roomCode,String id,String weight);

    //获得库房内部编号序列
    @Select("select * from ${arg0}")
    public List<RoomCubicle> selectAllCubicle(String tableName);

    //更新库房序列编号状态
    @Update("update ${arg0} set status=#{arg1},comment=#{arg2} where code=#{arg3}")
    public Boolean updateStatusByCode(String tableName,String status,String comment,String code);

    //入库更新库房序列编号状态
    @Update("update ${arg0} set status=#{arg1},comment=#{arg2},good_id=#{arg4} where code=#{arg3}")
    public Boolean updateRoomByCode(String tableName,String status,String comment,String code,String goodId);

    //根据暂存表id获得货物表中的id
    @Select("select * from ${arg0} where temp_id=#{arg1}")
    public ItemInformation selectByTempId(String tableName,String id);
}

