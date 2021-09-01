package com.ryq.coldstoragesystem.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ryq.coldstoragesystem.bean.Transport;
import com.ryq.coldstoragesystem.mapper.TransportMapper;
import com.ryq.coldstoragesystem.service.TransportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

@Service
public class TransportServiceImpl extends ServiceImpl<TransportMapper, Transport> implements TransportService {

    @Autowired
    TransportMapper transportMapper;

    @Override
    public boolean insertTransport(Transport transport) {
        return transportMapper.insertTransport(transport);
    }

    @Override
    public boolean updateTransportById(Transport transport) {
        return transportMapper.updateTransportById(transport);
    }

    @Override
    public boolean deleteTransportById(Integer id) {
        return transportMapper.deleteTransportById(id);
    }

    @Override
    public Transport selectTransportById(Integer id) {
        return transportMapper.selectTransportById(id);
    }

    @Override
    public List<Transport> selectAllTransport() {
        return transportMapper.selectAllTransport();
    }

    @Override
    public List<Transport> searchMinFee(String weight) {
        Transport transport = transportMapper.selectMinLessThanWeight(weight);
        List<Transport> transports = new ArrayList<>();
        if (transport != null){
            transport.setNumber("1");
            transports.add(transport);
            if (transport.getTruckType().equals("大型卡车")){
                Transport transport1 = new Transport(2,"中型卡车",14.00,"20",null,"0");
                Transport transport2 = new Transport(3,"小型卡车",6.00,"10",null,"0");
                transports.add(transport1);
                transports.add(transport2);
            }
            if (transport.getTruckType().equals("中型卡车")){
                Transport transport1 = new Transport(1,"大型卡车",100.00,"30",null,"0");
                Transport transport2 = new Transport(3,"小型卡车",6.00,"10",null,"0");
                transports.add(transport1);
                transports.add(transport2);
            }
            if (transport.getTruckType().equals("小型卡车")){
                Transport transport1 = new Transport(1,"大型卡车",100.00,"30",null,"0");
                Transport transport2 = new Transport(2,"中型卡车",14.00,"20",null,"0");
                transports.add(transport1);
                transports.add(transport2);
            }
        }else{
            double w = Double.parseDouble(weight);
            double number = Math.ceil(w/100.00);
            Transport transport1 = new Transport(1,"大型卡车",100.00,"30",null,String.valueOf((int)number));
            Transport transport2 = new Transport(2,"中型卡车",14.00,"20",null,"0");
            Transport transport3 = new Transport(3,"小型卡车",6.00,"10",null,"0");
            transports.add(transport1);
            transports.add(transport2);
            transports.add(transport3);
        }
        return transports;
    }

}
