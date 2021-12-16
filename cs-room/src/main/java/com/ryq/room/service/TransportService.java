package com.ryq.room.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ryq.coldstoragesystem.bean.Transport;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface TransportService extends IService<Transport> {

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

    public List<Transport> searchMinFee(String weight);
}
