package com.ryq.room.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ryq.coldstoragesystem.bean.WarehousingRecord;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface WarehousingRecordService extends IService<WarehousingRecord> {

    //根据操作员查询入库记录
    public List<WarehousingRecord> selectAllByOperatorWarehousingRecords(String operator);

    //查询所有入库记录
    public List<WarehousingRecord> selectAllWarehousingRecords();

    //查询
    //根据名称取出
    public List<WarehousingRecord> selectByItemName(String itemName);

    //根据编号取出
    public List<WarehousingRecord> selectByRoomCode(String roomCode);

    //根据名称取出相应操作员报表
    public List<WarehousingRecord> selectByItemNameAndOperator(String itemName,String operator);

    //根据编号取出相应操作员报表
    public List<WarehousingRecord> selectByRoomCodeAndOperator(String roomCode,String operator);
}
