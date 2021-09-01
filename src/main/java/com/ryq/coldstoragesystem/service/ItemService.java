package com.ryq.coldstoragesystem.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ryq.coldstoragesystem.bean.ItemInformation;
import com.ryq.coldstoragesystem.bean.RoomCubicle;
import com.ryq.coldstoragesystem.bean.TempSaveItems;

import java.util.List;

public interface ItemService extends IService<ItemInformation> {

    //新增货物信息
    //参数：货物表，表名
    public Boolean addItemInformtion(String tableName, String itemName, String itemWeight, String registrationTimeStamp,String operator,String identifier);

    //根据表名获取表内数据
    public List<ItemInformation> selectByTableName(String tableName);

    //存入审核表
    public Boolean addCheckitem(TempSaveItems tempSaveItems);

    //根据编号入库
    public Boolean insertByRoomCode(ItemInformation itemInformation,String roomCode);

    //根据id得到相应数据
    public ItemInformation selectById(String roomCode,String id);

    //根据库名和id删除相应数据
    public Boolean deleteByIdCustom(String roomCode,String id);

    //根据库名和id修改重量字段
    public Boolean updateById(String roomCode,String id,String weight);

    //获得库房内部编号序列
    public List<RoomCubicle> selectAllCubicle(String tableName);

    //更新库房序列编号状态
    public Boolean updateStatusByCode(String tableName,String status,String comment,String code);

    public Boolean updateRoomByCode(String tableName,String status,String comment,String code,String goodId);


    //根据暂存表id获得货物表中的id
    public ItemInformation selectByTempId(String tableName,String id);

}
