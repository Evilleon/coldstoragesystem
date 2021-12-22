package com.ryq.transport.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ryq.coldstoragesystem.bean.ItemInformation;
import com.ryq.coldstoragesystem.bean.TempSaveItems;
import com.ryq.coldstoragesystem.bean.User;
import com.ryq.coldstoragesystem.bean.WarehousingRecord;
import com.ryq.coldstoragesystem.service.ItemService;
import com.ryq.coldstoragesystem.service.TempSaveService;
import com.ryq.coldstoragesystem.service.WarehousingRecordService;
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
public class WarehousingRecordController {

    @Autowired
    WarehousingRecordService warehousingRecordService;
    @Autowired
    TempSaveService tempSaveService;
    @Autowired
    ItemService itemService;


    //查看报表（管理员可以查看所有报表）

    @GetMapping("/displaywarehousing")
    public String displayWarehousing(Model model){

//        Subject subject = SecurityUtils.getSubject();
//        if (subject.hasRole("admin")){
//            List<WarehousingRecord> warehousingRecords = warehousingRecordService.selectAllWarehousingRecords();
//            handleWarehousingRecords(model, warehousingRecords);
//        }else{
//            User user = (User)subject.getPrincipal();
//            List<WarehousingRecord> warehousingRecords = warehousingRecordService.selectAllByOperatorWarehousingRecords(user.getUserName());
//            handleWarehousingRecords(model, warehousingRecords);
//        }

        User user = (User)SecurityUtils.getSubject().getPrincipal();
        List<TempSaveItems> tempSaveItems = tempSaveService.selectAllByStatus(user.getUserName(), "5");
        for (TempSaveItems tempSaveItem : tempSaveItems){
            tempSaveItem.setDates(DateAndTimeStamp.timeStampToDate(tempSaveItem.getRegistrationTimeStamp()));
            ItemInformation item = itemService.selectByTempId(tempSaveItem.getRoomCode(), String.valueOf(tempSaveItem.getId()));
            tempSaveItem.setExDate(DateAndTimeStamp.timeStampToDate(item.getRegistrationTimeStamp()));
        }
        model.addAttribute("tempSaveItems",tempSaveItems);

        return "DisplayWarehousing";

    }

    private void handleWarehousingRecords(Model model, List<WarehousingRecord> warehousingRecords) {
        for (WarehousingRecord warehousingRecord:warehousingRecords){
            warehousingRecord.setInDates(DateAndTimeStamp.timeStampToDate(warehousingRecord.getRegistrationTimeStamp()));
            warehousingRecord.setInspectDates(DateAndTimeStamp.timeStampToDate(warehousingRecord.getInspectRegistrationTimeStamp()));
            if (warehousingRecord.getStatus().equals("1")){
                warehousingRecord.setStatusText("正常入库");
            }
        }
        model.addAttribute("warehousingRecords",warehousingRecords);
    }

    //搜索入库表
    @PostMapping("/searchwarehousing")
    @ResponseBody
    private JSONArray searchWarehousing(String searchType,String searchWord){
        List<TempSaveItems> tempSaveItems = new ArrayList<>();
        JSONArray jsonArray = new JSONArray();
        Subject subject = SecurityUtils.getSubject();
        User user = (User)subject.getPrincipal();
        if (searchType.equals("itemName")) {
            //从入库表找like货物名称的
            tempSaveItems = tempSaveService.selectByItemNameAndOperator(searchWord,user.getUserName(),"5");
        } else {
            //从入库表里找like库房编号的
            tempSaveItems = tempSaveService.selectByRoomCodeAndOperator(searchWord,user.getUserName(),"5");
        }
        System.out.println("tempSaveItems::::::::::::::::::::::"+tempSaveItems);
        if (tempSaveItems!=null){
            for (TempSaveItems tempSaveItem : tempSaveItems){
                tempSaveItem.setDates(DateAndTimeStamp.timeStampToDate(tempSaveItem.getRegistrationTimeStamp()));
                ItemInformation item = itemService.selectByTempId(tempSaveItem.getRoomCode(), String.valueOf(tempSaveItem.getId()));
                tempSaveItem.setExDate(DateAndTimeStamp.timeStampToDate(item.getRegistrationTimeStamp()));
                JSONObject jsonObject = (JSONObject) JSONObject.toJSON(tempSaveItem);
                jsonArray.add(jsonObject);
            }
        }
//        List<WarehousingRecord> warehousingRecords;
//        Subject subject = SecurityUtils.getSubject();
//        if (subject.hasRole("admin")) {
//            if (searchType.equals("itemName")) {
//                //从入库表找like货物名称的
//                warehousingRecords = warehousingRecordService.selectByItemName(searchWord);
//            } else {
//                //从入库表里找like库房编号的
//                warehousingRecords = warehousingRecordService.selectByRoomCode(searchWord);
//            }
//        }else{
//            User user = (User)subject.getPrincipal();
//            if (searchType.equals("itemName")) {
//                //从入库表找like货物名称的
//                warehousingRecords = warehousingRecordService.selectByItemNameAndOperator(searchWord,user.getUserName());
//            } else {
//                //从入库表里找like库房编号的
//                warehousingRecords = warehousingRecordService.selectByRoomCodeAndOperator(searchWord,user.getUserName());
//            }
//        }
//        handleWarehousingRecords(model, warehousingRecords);
        return jsonArray;
    }


}
