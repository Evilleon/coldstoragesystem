package com.ryq.transport.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ryq.coldstoragesystem.bean.DeliveryRecord;
import com.ryq.coldstoragesystem.mapper.DeliveryRecordMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DeliveryRecordServiceImpl extends ServiceImpl<DeliveryRecordMapper, DeliveryRecord> implements DeliveryRecordService {

    @Autowired
    DeliveryRecordMapper deliveryRecordMapper;
    //插入
    @Override
    public Boolean insertDeliveryRecord(DeliveryRecord deliveryRecord) {
        return deliveryRecordMapper.insertDeliveryRecord(deliveryRecord);
    }

    //根据operator取出
    @Override
    public List<DeliveryRecord> selectAllByOperatorDeliveryRecord(String operator) {
        return deliveryRecordMapper.selectAllByOperatorDeliveryRecord(operator);
    }

    //全部取出
    @Override
    public List<DeliveryRecord> selectAllDeliveryRecord() {
        return deliveryRecordMapper.selectAllDeliveryRecord();
    }

    @Override
    public List<DeliveryRecord> selectByItemName(String itemName) {
        return deliveryRecordMapper.selectByItemName(itemName);
    }

    @Override
    public List<DeliveryRecord> selectByRoomCode(String roomCode) {
        return deliveryRecordMapper.selectByRoomCode(roomCode);
    }

    @Override
    public List<DeliveryRecord> selectByItemNameAndOperator(String itemName, String operator) {
        return deliveryRecordMapper.selectByItemNameAndOperator(itemName,operator);
    }

    @Override
    public List<DeliveryRecord> selectByRoomCodeAndOperator(String roomCode, String operator) {
        return deliveryRecordMapper.selectByRoomCodeAndOperator(roomCode,operator);
    }
}
