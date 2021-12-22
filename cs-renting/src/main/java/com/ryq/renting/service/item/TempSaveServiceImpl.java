package com.ryq.renting.service.item;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ryq.coldstoragesystem.bean.TempSaveItems;
import com.ryq.coldstoragesystem.mapper.TempSaveMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TempSaveServiceImpl extends ServiceImpl<TempSaveMapper,TempSaveItems> implements TempSaveService {

    @Autowired
    TempSaveMapper tempSaveMapper;

    //根据请求审查员得到信息
    @Override
    public List<TempSaveItems> getAllByInspectorTempSaveItems(String inspector,String status) {
        List<TempSaveItems> allByInspectorTempSaveItems = tempSaveMapper.getAllByInspectorTempSaveItems(inspector,status);
        System.out.println(allByInspectorTempSaveItems);
        return allByInspectorTempSaveItems;
    }

    //一条一条处理
    @Override
    public TempSaveItems getOneByMinStamp(String inspector){
        return tempSaveMapper.getOneByMinStamp(inspector);
    }

    //根据用户名统计个数
    @Override
    public Integer countByInspectorInteger(String inspector) {
        return tempSaveMapper.countByInspectorInteger(inspector);
    }

    //根据id删除
    public Boolean deleteById(Integer id){
        return tempSaveMapper.deleteById(id);
    }

    //根据id更新temp数据
    public Boolean updateByIdCustom(TempSaveItems tempSaveItems){
        return tempSaveMapper.updateByIdCustom(tempSaveItems);
    }

    //根据id查询temp表
    public TempSaveItems selectAllByIdCustom(int id){
        return tempSaveMapper.selectAllByIdCustom(id);
    }

    //更新库房编号、状态、审查人员、提交人员
    public Boolean updateRoomCodeByIdCustom(TempSaveItems tempSaveItems){
        return tempSaveMapper.updateRoomCodeByIdCustom(tempSaveItems);
    }

    //得到operator
    @Override
    public List<TempSaveItems> getAllByOperatorTempSaveItems(String operator) {
        return tempSaveMapper.getAllByOperatorTempSaveItems(operator);
    }

    //更新重量，状态和库房编号
    @Override
    public Boolean updateStatusBytempId(String tempId, String itemWeight, String nums, String status) {
        return tempSaveMapper.updateStatusBytempId(tempId,itemWeight,nums,status);
    }

    //更新预约状态（取消预约）
    @Override
    public Boolean updateStatusById(String id, String status) {
        return tempSaveMapper.updateStatusById(id,status);
    }

    @Override
    public List<TempSaveItems> selectAllByStatus(String userName, String status) {
        return tempSaveMapper.selectAllByStatus(userName,status);
    }

    @Override
    public List<TempSaveItems> selectByItemNameAndOperator(String itemName, String operator,String status) {
        return tempSaveMapper.selectByItemNameAndOperator(itemName,operator,status);
    }

    @Override
    public List<TempSaveItems> selectByRoomCodeAndOperator(String roomCode, String operator,String status) {
        return tempSaveMapper.selectByRoomCodeAndOperator(roomCode,operator,status);
    }
}
