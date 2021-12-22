package com.ryq.renting.mapper.item;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ryq.coldstoragesystem.bean.ExportList;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface ExportListMapper extends BaseMapper<ExportList> {

    //插入
    public Boolean insertExportList(ExportList exportList);

    //根据当前登录用户名获得所有信息
    public List<ExportList> selectAllByUsername(String userName);

    //根据id查询
    public ExportList selectAllById(Integer id);

    //根据id更新状态
    public boolean updateStatusById(ExportList exportList);

    //根据tempId获得
    public ExportList selectByTempId(String id);

    //根据名称取出相应操作员报表
    public List<ExportList> selectByItemNameAndOperator(String itemName,String operator);

    //根据编号取出相应操作员报表
    public List<ExportList> selectByRoomCodeAndOperator(String roomCode,String operator);

}
