package com.ryq.transport.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ryq.coldstoragesystem.bean.DeliveryRecord;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface DeliveryRecordMapper extends BaseMapper<DeliveryRecord> {

    //插入
    public Boolean insertDeliveryRecord(DeliveryRecord deliveryRecord);

    //根据取出
    public List<DeliveryRecord> selectAllByOperatorDeliveryRecord(String operator);

    //全部取出
    public List<DeliveryRecord> selectAllDeliveryRecord();

    //根据名称取出所有报表
    public List<DeliveryRecord> selectByItemName(String itemName);

    //根据编号取出所有报表
    public List<DeliveryRecord> selectByRoomCode(String roomCode);

    //根据名称取出相应操作员报表
    public List<DeliveryRecord> selectByItemNameAndOperator(String itemName,String operator);

    //根据编号取出相应操作员报表
    public List<DeliveryRecord> selectByRoomCodeAndOperator(String roomCode,String operator);
}
