package com.ryq.coldstoragesystem.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ryq.coldstoragesystem.bean.RoomCubicle;
import com.ryq.coldstoragesystem.bean.TempSaveItems;
import com.ryq.coldstoragesystem.mapper.ItemInformationMapper;
import com.ryq.coldstoragesystem.service.ItemService;
import com.ryq.coldstoragesystem.bean.ItemInformation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ItemServiceImpl extends ServiceImpl<ItemInformationMapper, ItemInformation> implements ItemService {

    @Autowired
    ItemInformationMapper itemInformationMapper;

    @Override
    public Boolean addItemInformtion(String tableName, String itemName, String itemWeight, String registrationTimeStamp,String operator,String identifier) {
        return itemInformationMapper.addItemInformtion(tableName,itemName,itemWeight,registrationTimeStamp,operator,identifier);
    }

    @Override
    public List<ItemInformation> selectByTableName(String tableName) {
        return itemInformationMapper.selectByTableName(tableName);
    }
    //存入审核表
    @Override
    public Boolean addCheckitem(TempSaveItems tempSaveItems) {
        return itemInformationMapper.addCheckitem(tempSaveItems);
    }

    //根据编号入库
    @Override
    public Boolean insertByRoomCode(ItemInformation itemInformation, String roomCode) {
        return itemInformationMapper.insertByRoomCode(itemInformation,roomCode);
    }

    //根据id得到相应数据
    @Override
    public ItemInformation selectById(String roomCode,String id){
        return itemInformationMapper.selectById(roomCode,Integer.valueOf(id));
    }

    //根据库名和id删除相应数据
    @Override
    public Boolean deleteByIdCustom(String roomCode, String id) {
        return itemInformationMapper.deleteByIdCustom(roomCode,id);
    }

    //根据库名和id修改重量字段
    @Override
    public Boolean updateById(String roomCode, String id, String weight) {
        return itemInformationMapper.updateById(roomCode,id,weight);
    }

    //获得库房内部编号序列
    @Override
    public List<RoomCubicle> selectAllCubicle(String tableName) {
        return itemInformationMapper.selectAllCubicle(tableName);
    }

    //更新库房序列编号状态
    @Override
    public Boolean updateStatusByCode(String tableName, String status, String comment, String code) {
        return itemInformationMapper.updateStatusByCode(tableName,status,comment,code);
    }

    //入库更新库房序列编号状态
    @Override
    public Boolean updateRoomByCode(String tableName, String status, String comment, String code, String goodId) {
        return itemInformationMapper.updateRoomByCode(tableName,status,comment,code,goodId);
    }

    //根据暂存表id获得货物表中的id
    @Override
    public ItemInformation selectByTempId(String tableName, String id) {
        return itemInformationMapper.selectByTempId(tableName,id);
    }

}
