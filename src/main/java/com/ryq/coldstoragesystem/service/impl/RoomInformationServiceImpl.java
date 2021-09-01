package com.ryq.coldstoragesystem.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ryq.coldstoragesystem.bean.RoomCubicle;
import com.ryq.coldstoragesystem.mapper.RoomInformationMapper;
import com.ryq.coldstoragesystem.service.RoomInformationService;
import com.ryq.coldstoragesystem.bean.AddColdStorageRoomInformation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoomInformationServiceImpl extends ServiceImpl<RoomInformationMapper,AddColdStorageRoomInformation> implements RoomInformationService {
    @Autowired
    RoomInformationMapper roomInformationMapper;

    // 通过编号得到仓库
    public AddColdStorageRoomInformation getRoomByRoomCode(String roomCode){
        return  roomInformationMapper.getAllByRoomCode(roomCode);
    }
    //无条件得到仓库名称和仓库编号
    public List<AddColdStorageRoomInformation> getRoomNameAndCode(){
        return roomInformationMapper.getRoomNameAndCode();
    }
    //无条件的到全部仓库基本信息
    public List<AddColdStorageRoomInformation> getAllRoomInformation(){
        return roomInformationMapper.getAllRoomInformation();
    }
    // 新增仓库信息 mybatis
//    public void addRoomInformation(AddColdStorageRoomInformation addColdStorageRoomInformation){
//        roomInformationMapper.addRoomInformation(addColdStorageRoomInformation);
//    }

    //得到全部仓库编号
    public List<String> getRoomCode(){
        return roomInformationMapper.getRoomCode();
    }

    // 新增仓库信息 mybatis-plus
    public void addRoomInformation(AddColdStorageRoomInformation addColdStorageRoomInformation){
        roomInformationMapper.addRoomInformation(addColdStorageRoomInformation);
    }

    @Override
    public List<AddColdStorageRoomInformation> getAllByRoomLabelCode(String roomLabelCode) {
        return roomInformationMapper.getAllByRoomLabelCode(roomLabelCode);
    }

    @Override
    public Boolean updateVolumeAndLabel(String unusedCapacity, String usedCapacity, String labelName, String labelCode,String roomCode) {
        return roomInformationMapper.updateVolumeAndLabel(unusedCapacity,usedCapacity,labelName,labelCode,roomCode);
    }

    //更新库房信息字段，包括：未使用容积、已使用容积
    //参数，3个String型
    public Boolean updateVolume(String unusedCapacity,String usedCapacity,String roomCode){
        return roomInformationMapper.updateVolume(unusedCapacity,usedCapacity,roomCode);
    }

    //复制表
    @Override
    public Boolean creatTables(String tableName) {
        return roomInformationMapper.creatTables(tableName);
    }

    //复制仓库代码表
    @Override
    public Boolean createCodeTables(String tableName) {
        return roomInformationMapper.creatCodeTables(tableName);
    }

    //得到相应仓库编号
    @Override
    public String selectById(Integer id) {
        AddColdStorageRoomInformation addColdStorageRoomInformation = roomInformationMapper.selectById(id);
        return addColdStorageRoomInformation.getRoomCode();
    }

    //根据id查询冷库价格
    public String selectCostById(Integer id){
        AddColdStorageRoomInformation addColdStorageRoomInformation = roomInformationMapper.selectById(id);
        return addColdStorageRoomInformation.getRoomDailyCost();
    }

    // 根据id找到仓库基本信息
    @Override
    public AddColdStorageRoomInformation getAllById(Integer id) {
        return roomInformationMapper.getAllById(id);
    }

    @Override
    public Boolean updateByid(AddColdStorageRoomInformation addColdStorageRoomInformation) {
        return roomInformationMapper.updateByid(addColdStorageRoomInformation);
    }

    @Override
    public List<AddColdStorageRoomInformation> selectRoomByRoomName(String roomName) {
        return roomInformationMapper.selectRoomByRoomName(roomName);
    }

    @Override
    public List<AddColdStorageRoomInformation> selectRoomByRoomCode(String roomCode) {
        return roomInformationMapper.selectRoomByRoomCode(roomCode);
    }

    @Override
    public List<AddColdStorageRoomInformation> selectRoomByTemperature(String specialTemperature) {
        return roomInformationMapper.selectRoomByTemperature(specialTemperature);
    }

    @Override
    public List<AddColdStorageRoomInformation> selectRoomByCost(String specialCost) {
        return roomInformationMapper.selectRoomByCost(specialCost);
    }

    //根据标签编号取
    @Override
    public List<AddColdStorageRoomInformation> selectRoomByLabelCode(String roomLabelCode) {
        return roomInformationMapper.selectRoomByLabelCode(roomLabelCode);
    }

    @Override
    public Boolean insertCodeTables(List<RoomCubicle> roomCubicle, String roomCode) {
        return roomInformationMapper.insertCodeTables(roomCubicle,roomCode);
    }



}
