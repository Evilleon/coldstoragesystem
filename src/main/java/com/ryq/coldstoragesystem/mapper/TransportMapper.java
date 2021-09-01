package com.ryq.coldstoragesystem.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ryq.coldstoragesystem.bean.Transport;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface TransportMapper extends BaseMapper<Transport> {

    //新增运输工具
    public boolean insertTransport(Transport transport);

    //根据id修改运输工具
    public boolean updateTransportById(Transport transport);

    //根据id删除运输工具
    public boolean deleteTransportById(Integer id);

    //根据id查看运输工具
    public Transport selectTransportById(Integer id);

    //查看所有运输工具
    public List<Transport> selectAllTransport();

    //选择大于输入重量的最小型号
    public Transport selectMinLessThanWeight(String weight);

}
