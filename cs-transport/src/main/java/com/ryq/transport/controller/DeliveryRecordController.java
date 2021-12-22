package com.ryq.transport.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ryq.coldstoragesystem.bean.*;
import com.ryq.coldstoragesystem.service.DeliveryRecordService;
import com.ryq.coldstoragesystem.service.ExportListService;
import com.ryq.coldstoragesystem.utils.DateAndTimeStamp;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

@Controller
public class DeliveryRecordController {

    @Autowired
    DeliveryRecordService deliveryRecordService;

    @Autowired
    ExportListService exportListService;

    @GetMapping("/displaydelivery")
    public String displayDelivery(Model model){
        User user = (User)SecurityUtils.getSubject().getPrincipal();
        List<ExportList> exportLists = exportListService.selectAllByUsername(user.getUserName());
        model.addAttribute("exportLists",exportLists);

//        Subject subject = SecurityUtils.getSubject();
//        if (subject.hasRole("admin")){
//            List<DeliveryRecord> deliveryRecords = deliveryRecordService.selectAllDeliveryRecord();
//            handleDeliveryRecords(model, deliveryRecords);
//        }else{
//            User user = (User)subject.getPrincipal();
//            List<DeliveryRecord> deliveryRecords = deliveryRecordService.selectAllByOperatorDeliveryRecord(user.getUserName());
//            handleDeliveryRecords(model, deliveryRecords);
//        }
        return "DisplayDelivery";
    }

    //处理一下时间
    private void handleDeliveryRecords(Model model, List<DeliveryRecord> deliveryRecords) {
        for (DeliveryRecord deliveryRecord:deliveryRecords){
            deliveryRecord.setInDate(DateAndTimeStamp.timeStampToDate(deliveryRecord.getInRegistrationTimeStamp()));
            deliveryRecord.setExDate(DateAndTimeStamp.timeStampToDate(deliveryRecord.getExRegistrationTimeStamp()));
            if (deliveryRecord.getStatus().equals("2")){
                deliveryRecord.setStatusText("正常出库");
            }
        }
        model.addAttribute("deliveryRecords",deliveryRecords);
    }

    //搜索出库表
    @PostMapping("/searchdelivery")
    @ResponseBody
    private JSONArray searchDelivery(String searchType,String searchWord){

        List<ExportList> exportLists = new ArrayList<>();
        JSONArray jsonArray = new JSONArray();
        Subject subject = SecurityUtils.getSubject();
        User user = (User)subject.getPrincipal();
        if (searchType.equals("itemName")) {
            //从入库表找like货物名称的
            exportLists = exportListService.selectByItemNameAndOperator(searchWord,user.getUserName());
        } else {
            //从入库表里找like库房编号的
            exportLists = exportListService.selectByRoomCodeAndOperator(searchWord,user.getUserName());
        }
        System.out.println("exportLists::::::::::::::::::::::"+exportLists);
        if (exportLists!=null){
            for (ExportList exportList : exportLists){

                JSONObject jsonObject = (JSONObject) JSONObject.toJSON(exportList);
                jsonArray.add(jsonObject);
            }
        }

//        List<DeliveryRecord> deliveryRecords;
//        Subject subject = SecurityUtils.getSubject();
//        if (subject.hasRole("admin")) {
//            if (searchType.equals("itemName")) {
//                //从出库表找like货物名称的
//                deliveryRecords = deliveryRecordService.selectByItemName(searchWord);
//            } else {
//                //从出库表里找like库房编号的
//                deliveryRecords = deliveryRecordService.selectByRoomCode(searchWord);
//            }
//        }else{
//            User user = (User)subject.getPrincipal();
//            if (searchType.equals("itemName")) {
//                //从出库表找like货物名称的
//                deliveryRecords = deliveryRecordService.selectByItemNameAndOperator(searchWord,user.getUserName());
//            } else {
//                //从出库表里找like库房编号的
//                deliveryRecords = deliveryRecordService.selectByRoomCodeAndOperator(searchWord,user.getUserName());
//            }
//        }
//        handleDeliveryRecords(model, deliveryRecords);
        return jsonArray;
    }

}
