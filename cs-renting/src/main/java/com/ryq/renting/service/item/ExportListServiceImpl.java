package com.ryq.renting.service.item;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ryq.coldstoragesystem.bean.ExportList;
import com.ryq.coldstoragesystem.mapper.ExportListMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExportListServiceImpl extends ServiceImpl<ExportListMapper, ExportList> implements ExportListService {

    @Autowired
    ExportListMapper exportListMapper;

    @Override
    public Boolean insertExportList(ExportList exportList) {
        return exportListMapper.insertExportList(exportList);
    }

    //根据当前登录用户名获得所有信息
    @Override
    public List<ExportList> selectAllByUsername(String userName) {
        return exportListMapper.selectAllByUsername(userName);
    }

    //根据id查询
    @Override
    public ExportList selectAllById(Integer id) {
        return exportListMapper.selectAllById(id);
    }

    //根据id更新状态
    @Override
    public boolean updateStatusById(ExportList exportList) {
        return exportListMapper.updateStatusById(exportList);
    }

    //根据tempId获得
    @Override
    public ExportList selectByTempId(String id) {
        return exportListMapper.selectByTempId(id);
    }

    @Override
    public List<ExportList> selectByItemNameAndOperator(String itemName, String operator) {
        return exportListMapper.selectByItemNameAndOperator(itemName,operator);
    }

    @Override
    public List<ExportList> selectByRoomCodeAndOperator(String roomCode, String operator) {
        return exportListMapper.selectByRoomCodeAndOperator(roomCode,operator);
    }


}
