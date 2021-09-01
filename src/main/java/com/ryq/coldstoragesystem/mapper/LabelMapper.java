package com.ryq.coldstoragesystem.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ryq.coldstoragesystem.bean.RoomLabel;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.data.relational.core.sql.In;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface LabelMapper extends BaseMapper<RoomLabel> {

    // 无条件列举可用标签
    @Select("select * from room_label")
    public List<RoomLabel> getLabels();

    //删除标签
    @Delete("delete from room_label where id=#{arg0}")
    public boolean deleteById(Integer id);

    //根据标签码得到标签
    @Select("select * from room_label where room_label_code=#{arg0}")
    public RoomLabel getLabelByCode(String labelRoomCode);
}
