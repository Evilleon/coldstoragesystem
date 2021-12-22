package com.ryq.renting.service.item;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ryq.coldstoragesystem.bean.Transfer;
import org.springframework.stereotype.Service;

@Service
public interface TransferService extends IService<Transfer> {

    //向转存表中插入
    public Boolean insertTransfer(Transfer transfer);
}
