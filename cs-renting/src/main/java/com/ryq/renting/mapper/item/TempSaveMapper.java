package com.ryq.renting.mapper.item;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ryq.coldstoragesystem.bean.TempSaveItems;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface TempSaveMapper extends BaseMapper<TempSaveItems> {

    //根据inspector得到信息
    @Select("select * from checking_items where inspector=#{arg0} and status=#{arg1}")
    public List<TempSaveItems> getAllByInspectorTempSaveItems(String inspector,String status);

    //根据用户名统计个数
    @Select("SELECT COUNT(*) from checking_items WHERE inspector=#{arg0}")
    public Integer countByInspectorInteger(String inspector);

    //一条一条读取并处理
    @Select("SELECT * from checking_items where registration_time_stamp=(SELECT min(registration_time_stamp) from checking_items)AND inspector=#{arg0} AND status=1 ORDER BY id LIMIT 1;")
    public TempSaveItems getOneByMinStamp(String inspector);

    //根据id删除
    @Delete("delete from checking_items where id=#{id}")
    public Boolean deleteById(Integer id);

    //根据id更新temp数据
    public Boolean updateByIdCustom(TempSaveItems tempSaveItems);

    //根据id查询temp表
    public TempSaveItems selectAllByIdCustom(int id);

    //更新库房编号、状态、审查人员、提交人员
    public Boolean updateRoomCodeByIdCustom(TempSaveItems tempSaveItems);

    //更新重量，状态和库房编号
    @Update("update checking_items set item_weight=#{arg1},room_point=#{arg2},status=#{arg3} where id=#{arg0}")
    public Boolean updateStatusBytempId(String tempId,String itemWeight,String nums,String status);

    //得到operator
    @Select("select * from checking_items where operator=#{arg0}")
    public List<TempSaveItems> getAllByOperatorTempSaveItems(String operator);

    //更新预约状态（取消预约）
    @Update("update checking_items set status=#{arg1} where id=#{arg0}")
    public Boolean updateStatusById(String id, String status);

    //根据用户名和状态获得所有货物
    public List<TempSaveItems> selectAllByStatus(String userName, String status);

    //根据名称取出相应操作员报表
    public List<TempSaveItems> selectByItemNameAndOperator(String itemName,String operator,String status);

    //根据编号取出相应操作员报表
    public List<TempSaveItems> selectByRoomCodeAndOperator(String roomCode,String operator,String status);
}
