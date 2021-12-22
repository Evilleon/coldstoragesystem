package com.ryq.room.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ryq.common.room.entity.TempSaveItems;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface TempSaveService {

    //根据inspector得到信息
    public List<TempSaveItems> getAllByInspectorTempSaveItems(String inspector,String status);

    //一条一条处理
    public TempSaveItems getOneByMinStamp(String inspector);

    //根据用户名统计个数
    public Integer countByInspectorInteger(String inspector);

    //根据id删除
    public Boolean deleteById(Integer id);

    //根据id更新temp数据
    public Boolean updateByIdCustom(TempSaveItems tempSaveItems);

    //根据id查询temp表
    public TempSaveItems selectAllByIdCustom(int id);

    //更新库房编号、状态、审查人员、提交人员
    public Boolean updateRoomCodeByIdCustom(TempSaveItems tempSaveItems);

    //得到operator
    public List<TempSaveItems> getAllByOperatorTempSaveItems(String operator);

    //更新重量，状态和库房编号
    public Boolean updateStatusBytempId(String tempId,String itemWeight,String nums,String status);

    //更新预约状态（取消预约）
    public Boolean updateStatusById(String id,String status);

    //更新预约状态（取消预约）
    public List<TempSaveItems> selectAllByStatus(String userName,String status);


    //根据名称取出相应操作员报表
    public List<TempSaveItems> selectByItemNameAndOperator(String itemName,String operator,String status);

    //根据编号取出相应操作员报表
    public List<TempSaveItems> selectByRoomCodeAndOperator(String roomCode,String operator,String status);

}

