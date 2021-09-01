package com.ryq.coldstoragesystem.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ryq.coldstoragesystem.bean.WarehousingRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface WarehousingRecordMapper extends BaseMapper<WarehousingRecord> {


    //根据操作员查询入库记录
    public List<WarehousingRecord> selectAllByOperatorWarehousingRecords(String operator);

    //查询所有入库记录
    @Select("select * from sjz_warehousing_record")
    public List<WarehousingRecord> selectAllWarehousingRecords();

    //根据名称取出所有报表
    public List<WarehousingRecord> selectByItemName(String itemName);

    //根据编号取出所有报表
    public List<WarehousingRecord> selectByRoomCode(String roomCode);

    //根据名称取出相应操作员报表
    public List<WarehousingRecord> selectByItemNameAndOperator(String itemName,String operator);

    //根据编号取出相应操作员报表
    public List<WarehousingRecord> selectByRoomCodeAndOperator(String roomCode,String operator);
}
