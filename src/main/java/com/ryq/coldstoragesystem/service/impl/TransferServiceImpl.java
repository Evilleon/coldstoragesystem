package com.ryq.coldstoragesystem.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ryq.coldstoragesystem.bean.Transfer;
import com.ryq.coldstoragesystem.mapper.TransferMapper;
import com.ryq.coldstoragesystem.service.TransferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TransferServiceImpl extends ServiceImpl<TransferMapper, Transfer> implements TransferService {

    @Autowired
    TransferMapper transferMapper;

    //插入转存库表
    @Override
    public Boolean insertTransfer(Transfer transfer) {
        return transferMapper.insertTransfer(transfer);
    }
}
