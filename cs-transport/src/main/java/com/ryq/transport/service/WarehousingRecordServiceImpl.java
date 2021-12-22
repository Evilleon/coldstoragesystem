package com.ryq.transport.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ryq.coldstoragesystem.bean.WarehousingRecord;
import com.ryq.coldstoragesystem.mapper.WarehousingRecordMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WarehousingRecordServiceImpl extends ServiceImpl<WarehousingRecordMapper, WarehousingRecord> implements WarehousingRecordService {

    @Autowired
    WarehousingRecordMapper warehousingRecordMapper;

    //根据操作员查询入库记录
    @Override
    public List<WarehousingRecord> selectAllByOperatorWarehousingRecords(String operator) {
        return warehousingRecordMapper.selectAllByOperatorWarehousingRecords(operator);
    }

    //查询所有入库记录
    @Override
    public List<WarehousingRecord> selectAllWarehousingRecords() {
        return warehousingRecordMapper.selectAllWarehousingRecords();
    }

    @Override
    public List<WarehousingRecord> selectByItemName(String itemName) {
        return warehousingRecordMapper.selectByItemName(itemName);
    }

    @Override
    public List<WarehousingRecord> selectByRoomCode(String roomCode) {
        return warehousingRecordMapper.selectByRoomCode(roomCode);
    }

    @Override
    public List<WarehousingRecord> selectByItemNameAndOperator(String itemName, String operator) {
        return warehousingRecordMapper.selectByItemNameAndOperator(itemName,operator);
    }

    @Override
    public List<WarehousingRecord> selectByRoomCodeAndOperator(String roomCode, String operator) {
        return warehousingRecordMapper.selectByRoomCodeAndOperator(roomCode,operator);
    }
}
