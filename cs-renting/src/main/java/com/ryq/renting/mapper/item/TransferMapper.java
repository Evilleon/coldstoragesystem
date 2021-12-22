package com.ryq.renting.mapper.item;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ryq.coldstoragesystem.bean.Transfer;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface TransferMapper extends BaseMapper<Transfer> {

    //向转存表中插入
    public Boolean insertTransfer(Transfer transfer);
}
