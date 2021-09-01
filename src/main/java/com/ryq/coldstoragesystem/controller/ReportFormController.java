package com.ryq.coldstoragesystem.controller;

import com.ryq.coldstoragesystem.service.DeliveryRecordService;
import com.ryq.coldstoragesystem.service.WarehousingRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class ReportFormController {

    @Autowired
    WarehousingRecordService warehousingRecordService;

    @Autowired
    DeliveryRecordService deliveryRecordService;


    public String displayWarehousingRecord(){
        return null;
    }


}
