package com.ryq.coldstoragesystem.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ryq.coldstoragesystem.bean.DeliveryRecord;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface DeliveryRecordService extends IService<DeliveryRecord> {

    //插入
    public Boolean insertDeliveryRecord(DeliveryRecord deliveryRecord);

    //根据operator取出
    public List<DeliveryRecord> selectAllByOperatorDeliveryRecord(String operator);

    //全部取出
    public List<DeliveryRecord> selectAllDeliveryRecord();

    //根据名称取出
    public List<DeliveryRecord> selectByItemName(String itemName);

    //根据编号取出
    public List<DeliveryRecord> selectByRoomCode(String roomCode);

    //根据名称取出相应操作员报表
    public List<DeliveryRecord> selectByItemNameAndOperator(String itemName,String operator);

    //根据编号取出相应操作员报表
    public List<DeliveryRecord> selectByRoomCodeAndOperator(String roomCode,String operator);
}
