package com.ryq.room.controller;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ryq.common.room.entity.ItemInformation;
import com.ryq.common.room.entity.RoomLabel;
import com.ryq.common.room.entity.TempSaveItems;
import com.ryq.common.utils.DateAndTimeStamp;
import com.ryq.common.utils.GenerateIdentifier;
import com.ryq.common.utils.VerifyData;
import com.ryq.room.service.*;
import org.apache.shiro.SecurityUtils;
import org.apache.tomcat.util.buf.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

@Controller
public class ItemInformationController<getMinVolume> {

    @Autowired
    RoomInformationService roomInformationService;

    @Autowired
    LabelService labelService;

    @Autowired
    ItemService itemService;

    @Autowired
    LoginService loginService;

    @Autowired
    TempSaveService tempSaveService;

    @Autowired
    WarehousingRecordService warehousingRecordService;

    @Autowired
    UserService userService;

    @Autowired
    TransferService transferService;


    /**
     * 添加货物入库时，选择货物标签，点击提交
     * 提交后流程：根据标签检索总仓库表--->{1、检索无匹配，寻找可用容积不为0的最小容量仓库，比较货物吨数与仓库
     * 可存放吨数{1、仓库容量>货物吨数，直接放入该仓库表中，并修改总仓表可用、已用容量和仓库标签。
     * 2、仓库容量<货物吨数，依旧寻找可用容积不为0的最小容量仓库，并重复判断货物吨数与仓库可存放吨数}2、检索匹配
     * 标签，查询总库表该库可用容量，然后重复
     *
     */

    @GetMapping("/itemssave")
    public String costSave(Model model, @Valid ItemInformation itemInformation, BindingResult bindingResult) throws ParseException {
        HashMap<String, String> errorMap = new HashMap<>();
        /**
         * 传递进来的参数：名称，重量，标签号，仓号（可能省去）
         */
        //验证数据合法性
        if(bindingResult.hasErrors()){
            errorMap = VerifyData.validData(errorMap,bindingResult);
            List<RoomLabel> labelInformation = labelService.list();
            model.addAttribute("labelinformation",labelInformation);
            model.addAttribute("errorMap",errorMap);
            model.addAttribute("currentdata",itemInformation);
            return "AddColdStorageItem";
        }
        else {
            //获取当前时间,生成时间戳
            String RegistrationTimeStamp = DateAndTimeStamp.dateToTimeStamp();
            itemInformation.setRegistrationTimeStamp(RegistrationTimeStamp);

            //开始匹配
            String s = OptimalMatchingRoom(model, itemInformation);
            if (s==null){
                return "index";
            }
            else if (s.equals("-1")){
                List<RoomLabel> labelInformation = labelService.list();
                model.addAttribute("labelinformation",labelInformation);
                model.addAttribute("currentdata",itemInformation);
                model.addAttribute("msg","没有合适温度的库房啦！请尝试使用手动入库");
                return "AddColdStorageItem";
            }else if (s.equals("-2")){
                List<RoomLabel> labelInformation = labelService.list();
                model.addAttribute("labelinformation",labelInformation);
                model.addAttribute("currentdata",itemInformation);
                model.addAttribute("msg","没有合适库房啦！请尝试使用手动入库");
                return "AddColdStorageItem";
            }
            return "index";
            //参数会自动放在请求域中
//        model.addAttribute("costparameters",itemInformation);
            //return "CostManagement";
        }
    }

    /**
     * 匹配放入库存算法
     */
    public String OptimalMatchingRoom(Model model, ItemInformation itemInformation){
        //开始判断是否存在相同的标签
        //List<AddColdStorageRoomInformation> sameLabel = roomInformationService.lambdaQuery().eq(AddColdStorageRoomInformation::getRoomLabelCode, itemInformation.getStorageRoom().getRoomLabelCode()).list();
        List<AddColdStorageRoomInformation> sameLabel = roomInformationService.getAllByRoomLabelCode(itemInformation.getRoomLabel().getRoomLabelCode());
        System.out.println("sameLabel:"+sameLabel);
        System.out.println(itemInformation);
        User user = (User) SecurityUtils.getSubject().getPrincipal();
        String operator = user.getUserName();
        itemInformation.setOperator(operator);
        String identifier = GenerateIdentifier.generateIdentifier(itemInformation.getRoomLabel().getRoomLabelCode());
        itemInformation.setIdentifier(identifier);
        //当前没有与传递的参数相同的标签
        if(sameLabel.size() == 0){
            List<RoomLabel> allLabel = labelService.list();
            //得到相应的温度
            List<Integer> allTemProp = getMaxMinTem(allLabel,itemInformation);

            //筛选库房,1、标签为空 2、小于等于最高温度 3、大于等于最低温度
            List<AddColdStorageRoomInformation> allRoominformation = roomInformationService.lambdaQuery().eq(AddColdStorageRoomInformation::getRoomLabelCode,0).ge(AddColdStorageRoomInformation::getRoomTemperature,allTemProp.get(2)).le(AddColdStorageRoomInformation::getRoomTemperature,allTemProp.get(1)).list();
            System.out.println("++++++++++++++++++++++=============");
            allRoominformation.forEach(System.out::println);
            System.out.println("++++++++++++++++++++++=============");
            if(allRoominformation.size() == 0){
                model.addAttribute("msg","没有多余的库房啦！请联系管理员");
                return "-1";
            }
            //flag就是可用的仓库的index,有可能不存在
            Integer flag = getMinVolume(allRoominformation, allLabel, itemInformation);
            if(flag == -1){
                model.addAttribute("msg","存在"+itemInformation.getItemWeight()+"吨"+itemInformation.getItemName()+"未入库，\n原因：没有合适的库房啦！请联系管理员");
                return "-2";
            }
            //传递的货物重量
            Double transWeight = Double.parseDouble(itemInformation.getItemWeight());
            //未使用的货物重量
            Double unusedWeight = Double.parseDouble(allRoominformation.get(flag).getUnusedCapacity());

            //根据flag得到的仓库可用容积和仓库未用判断货物大小和单位，
            if(transWeight<=unusedWeight){
                //设置上传数据库的仓库信息：未使用容积、已使用容积、标签、标签代号
                Double usedCapacity = transWeight;
                Double unusedCapacity = unusedWeight -transWeight;
                String labelName = allLabel.get(getMaxMinTem(allLabel,itemInformation).get(0)).getRoomLabelName();
                String labelCode = allLabel.get(getMaxMinTem(allLabel,itemInformation).get(0)).getRoomLabelCode();
                String roomCode = allRoominformation.get(flag).getRoomCode();
                System.out.println("仓库特殊信息如下："+usedCapacity+"==="+unusedCapacity+"==="+labelName+"==="+labelCode+"==="+roomCode);
                //更新
                Boolean isUpdateRes = roomInformationService.updateVolumeAndLabel(String.valueOf(unusedCapacity), String.valueOf(usedCapacity), labelName, labelCode,roomCode);
                //设置上传数据库的货物：表名，货物名称，货物重量，时间戳
                itemService.addItemInformtion(roomCode,itemInformation.getItemName(),itemInformation.getItemWeight(),itemInformation.getRegistrationTimeStamp(),operator,identifier);
                insertNewRecord(itemInformation,roomCode,itemInformation.getRoomLabel());
            }
            //当参数重量大于库存重量时
            else {
                Double usedCapacity = Double.parseDouble(allRoominformation.get(flag).getUnusedCapacity());
                System.out.println("usedCapacity::"+usedCapacity);
                Double unusedCapacity = 0.00;
                String labelName = allLabel.get(getMaxMinTem(allLabel,itemInformation).get(0)).getRoomLabelName();
                String labelCode = allLabel.get(getMaxMinTem(allLabel,itemInformation).get(0)).getRoomLabelCode();
                String restCapacity = String.valueOf(transWeight-unusedWeight);//剩余未保存的重量
                String roomCode = allRoominformation.get(flag).getRoomCode();
                //设置上传数据库的仓库信息：未使用容积、已使用容积、标签、标签代号
                //更新
                Boolean isUpdateResRoom = roomInformationService.updateVolumeAndLabel(String.valueOf(unusedCapacity), String.valueOf(usedCapacity), labelName, labelCode,roomCode);
                System.out.println("参数重量大于库存重量，仓库表更新结果："+isUpdateResRoom);
                //设置上传数据库的货物：表名，货物名称，货物重量，时间戳
                Boolean isUpdateResItem = itemService.addItemInformtion(roomCode, itemInformation.getItemName(), String.valueOf(unusedWeight), itemInformation.getRegistrationTimeStamp(),operator,identifier);
                insertNewRecord(itemInformation,roomCode,itemInformation.getRoomLabel());
                System.out.println("参数重量大于库存重量，物品表更新结果："+isUpdateResItem);
                itemInformation.setItemWeight(restCapacity);
                OptimalMatchingRoom(model,itemInformation);
            }
        }
        //存在一个或多个相同的标签
        else{
            //sameLabel中保存着标签相同且未用完的库房
            //传递的货物重量
            Double mid =0.0,minWeight =Double.parseDouble(sameLabel.get(0).getUnusedCapacity());
            Double transWeight = Double.parseDouble(itemInformation.getItemWeight());
            //记录当前sameLabel的index值
            Integer flag = 0;
            //未使用的货物重量
            for (int each_tem = 0;each_tem<sameLabel.size();each_tem++){
                mid = Double.parseDouble(sameLabel.get(each_tem).getUnusedCapacity());
                if(minWeight > mid){
                    minWeight = mid;
                    flag = each_tem;
                }
            }
            Double unusedWeight = minWeight;//选出最小的未用容量
            //传递的值小于可容纳的值
            if(transWeight<=unusedWeight){
                Double usedCapacity = Double.parseDouble(sameLabel.get(flag).getUsedCapacity())+transWeight;//已用容量 = 之前已经有的容量+准备新放入的容量
                Double unusedCapacity = unusedWeight -transWeight;//剩余容量（未用容量） = （旧的）未用容量-准备传入的容量
                String roomCode = sameLabel.get(flag).getRoomCode();
                System.out.println("仓库特殊信息如下："+usedCapacity+"==="+unusedCapacity+"==="+roomCode);
                //修改该仓库可用容量和和已用容量的大小
                roomInformationService.updateVolume(String.valueOf(unusedCapacity),String.valueOf(usedCapacity),roomCode);
                //添加该仓库物品表
                //设置上传数据库的货物：表名，货物名称，货物重量，时间戳
                itemService.addItemInformtion(roomCode,itemInformation.getItemName(),itemInformation.getItemWeight(),itemInformation.getRegistrationTimeStamp(),operator,identifier);
                insertNewRecord(itemInformation,roomCode,itemInformation.getRoomLabel());
            }
            //传递的值大于于可容纳的值（已有标签）
            //例 仓库未用容量500，已用容量200，准备传递容量600
            else{
                Double usedCapacity = Double.parseDouble(sameLabel.get(flag).getRoomVolume());//新的已用容量 = 仓库总容量
                System.out.println("usedCapacity::"+usedCapacity);
                Double unusedCapacity = 0.00;//新的未用容量 = 0
                String restCapacity = String.valueOf(transWeight-unusedWeight);//剩余未保存的重量 = 传递容量-未用容量
                String roomCode = sameLabel.get(flag).getRoomCode();
                Boolean isUpdateResRoom = roomInformationService.updateVolume(String.valueOf(unusedCapacity), String.valueOf(usedCapacity),roomCode);
                System.out.println("参数重量大于库存重量，仓库表更新结果："+isUpdateResRoom);
                //设置上传数据库的货物：表名，货物名称，货物重量，时间戳
                Boolean isUpdateResItem = itemService.addItemInformtion(roomCode, itemInformation.getItemName(), String.valueOf(unusedWeight), itemInformation.getRegistrationTimeStamp(),operator,identifier);
                insertNewRecord(itemInformation,roomCode,itemInformation.getRoomLabel());
                System.out.println("参数重量大于库存重量，物品表更新结果："+isUpdateResItem);
                itemInformation.setItemWeight(restCapacity);
                OptimalMatchingRoom(model,itemInformation);
            }
        }
        return null;
    }
    /**
     * 得到(合适温度的)最小容量的冷库
     */
    public Integer getMinVolume(List<AddColdStorageRoomInformation> allRoominformation,List<RoomLabel> allLabel,ItemInformation itemInformation){
        Integer each_room = 1, minVolume = Integer.valueOf(allRoominformation.get(0).getRoomVolume()), midVolume = 0,flag = 0;

        Integer roomSize = allRoominformation.size();
        //循环查找 容量最小
        for(;each_room<roomSize;each_room++){

            midVolume = Integer.valueOf(allRoominformation.get(each_room).getRoomVolume());
            //筛选最小容积库房
            if(minVolume > midVolume) {
                minVolume = midVolume;
                flag = each_room;
            }
        }

        return flag;
    }
    /**
     * 通过标签找到最大最小温度
     */
    public List<Integer> getMaxMinTem(List<RoomLabel> allLabel,ItemInformation itemInformation){
        List<Integer> labelprop = new ArrayList<>();
        int labelSize = allLabel.size();
        //设置锚点，确定可进入条件判断的index
        int anchorPoint = 0;
        int each_label = 0;
        //得到本次传参的最低温度
        Integer maxTem = 0,minTem = 0;
        //找到与标签编号匹配的最低最高温度
        for(;each_label<labelSize;each_label++){
            if(itemInformation.getRoomLabel().getRoomLabelCode().equals(allLabel.get(each_label).getRoomLabelCode())){
                maxTem = Integer.parseInt(allLabel.get(each_label).getMaxTemperature());
                minTem = Integer.parseInt(allLabel.get(each_label).getMinTemperature());
                anchorPoint = each_label;
            }
        }
        labelprop.add(anchorPoint);
        labelprop.add(maxTem);
        labelprop.add(minTem);

        System.out.println("labelprop:"+labelprop);
        return labelprop;

    }

    @GetMapping("/test")
    public String testEx(){
        List<AddColdStorageRoomInformation> sameLabel = roomInformationService.lambdaQuery().eq(AddColdStorageRoomInformation::getRoomLabelName, null).list();
        System.out.println(sameLabel);
        return null;
    }

    /**
     *
     * @param model
     * @return
     */
    @GetMapping("/addcoldstorageitem")
    public String AddColdStorageItem(Model model, ItemInformation itemInformation){
        List<RoomLabel> labelInformation = labelService.list();

        //得到仓库名称和编号
//        List<AddColdStorageRoomInformation> nameAndCode = roomInformationService.getRoomNameAndCode();
//        model.addAttribute("nameAndCode",nameAndCode);
        model.addAttribute("currentdata",itemInformation);
        model.addAttribute("labelinformation",labelInformation);
        model.addAttribute("titleText","新增冷藏项目");
        return "AddColdStorageItem";
    }



    /**
     * 根据仓库编号返回该仓库基本信息
     * @param
     * @param model
     * @return
     */
//    @GetMapping("/roominformationdisplay")
//    public String getRoomByRoomCode(@RequestParam("roomCode") String roomCode,Model model){
//        List<AddColdStorageRoomInformation> ri = roomInformationService.getRoomByRoomCode(roomCode);
//        model.addAttribute("roominfor",ri);
//        return "RoomInformationDisplay";
//    }




    //手动选择入库信息
    @GetMapping("/manualaddcolditem")
    public String ManualAddColdItem(Model model, TempSaveItems tempSaveItems){
        List<RoomLabel> labelInformation = labelService.list();
        List<AddColdStorageRoomInformation> roomInformations = SplicingNameCode();

        //得到仓库名称和编号
//        List<AddColdStorageRoomInformation> nameAndCode = roomInformationService.getRoomNameAndCode();
//        model.addAttribute("nameAndCode",nameAndCode);
        ItemInformation itemInformation = new ItemInformation();
        itemInformation.setItemName("");
        itemInformation.setItemWeight("");
        List<User> inspectors = userService.selectAllInspectors();
        //tempSaveItems.setItemInformation(itemInformation);
        model.addAttribute("currentdata",tempSaveItems);
        model.addAttribute("labelinformation",labelInformation);
        model.addAttribute("roomInformation",roomInformations);
        model.addAttribute("inspectors",inspectors);
        return "ManualAddColdItem";
    }

    //入审核表
    //@ReWrite
    @PostMapping("/itemsmanualsave")
    @ResponseBody
    public Boolean ManualSaveItems(Model model, TempSaveItems tempSaveItems) throws ParseException {
        //        HashMap<String, String> errorMap = new HashMap<>();

//
//        List<RoomLabel> labelInformation = labelService.list();
//        List<AddColdStorageRoomInformation> roomInformation = SplicingNameCode();
//        List<User> inspectors = userService.selectAllInspectors();
//        //存在输入错误
//        if (bindingResult.hasErrors()){
//            errorMap = VerifyData.validData(errorMap, bindingResult);
//            model.addAttribute("roomInformation",roomInformation);
//            model.addAttribute("labelinformation",labelInformation);
//            model.addAttribute("errorMap",errorMap);
//            model.addAttribute("inspectors",inspectors);
//            model.addAttribute("currentdata",tempSaveItems);
//            return "ManualAddColdItem";
//        }
////        if (itemInformation.getItemWeight()>itemInformation.getStorageRoom().getRoomCode()){
////
////        }
//        //比较容量大小
//        List<AddColdStorageRoomInformation> list = roomInformationService.lambdaQuery().eq(AddColdStorageRoomInformation::getRoomCode, tempSaveItems.getRoomCode()).list();
//        if (Long.parseLong(tempSaveItems.getItemWeight())>Long.parseLong(list.get(0).getRoomVolume())){
//            errorMap.put("itemWeight","容量不足，请选择其他冷藏室或使用自动入库");
//            model.addAttribute("roomInformation",roomInformation);
//            model.addAttribute("labelinformation",labelInformation);
//            model.addAttribute("errorMap",errorMap);
//            model.addAttribute("inspectors",inspectors);
//            model.addAttribute("currentdata",tempSaveItems);
//            return "ManualAddColdItem";
//        }
        //规定编号
        //编号命名规则：操作员编号+入库物品编号+库房编号（数字部分）+当前时间（秒）
        String identifier = GenerateIdentifier.generateIdentifier(tempSaveItems.getRoomLabelCode(), tempSaveItems.getRoomCode());
        //没有错误，参数设置后，进行中转存入checking_item表
        User user = (User) SecurityUtils.getSubject().getPrincipal();
        //tempSaveItems.setItemInformation(itemInformation);
        tempSaveItems.setOperator(user.getUserName());
        tempSaveItems.setStatus("1");//请求审核：1；驳回审核：2
        tempSaveItems.setRegistrationTimeStamp(DateAndTimeStamp.dateToTimeStamp());
        tempSaveItems.setIdentifier(identifier);
        //根据标签编号获取标签名称
        RoomLabel labelByCode = labelService.getLabelByCode(tempSaveItems.getRoomLabelCode());
        tempSaveItems.setRoomLabelName(labelByCode.getRoomLabelName());
        System.out.println(tempSaveItems);

        return itemService.addCheckitem(tempSaveItems);
    }

    //合并名称-编号-温度
    public List<AddColdStorageRoomInformation> SplicingNameCode(){
        List<AddColdStorageRoomInformation> roomInformation = roomInformationService.list();
        String roomName,roomCode,unusedCapacity;
        for (AddColdStorageRoomInformation list:roomInformation){
            if (!(list.getUnusedCapacity()).equals("0")) {
                roomName = list.getRoomName();
                roomCode = list.getRoomCode();
                unusedCapacity = list.getUnusedCapacity();
                list.setRoomName(roomName + "-" + roomCode + "-" + unusedCapacity);
            }
        }
        return roomInformation;
    }
    //查询是否有要入库审批的项目
    @GetMapping("/querycheckitem")
    public String QueryCheckItem(Model model){
        //得到当前用户信息
        User logInformation = (User) SecurityUtils.getSubject().getPrincipal();
        String userName = logInformation.getUserName();

//        List<User> list = loginService.lambdaQuery().eq(User::getUserName, userName).list();
//        System.out.println(list.get(0));
        //获得
        List<TempSaveItems> list = tempSaveService.getAllByInspectorTempSaveItems(userName,"1");
        if (!(list.size()==0)){
            for (TempSaveItems each : list){
                each.setItemWeight("(空)");
                each.setDates(DateAndTimeStamp.timeStampToDate(each.getRegistrationTimeStamp()));
                List<AddColdStorageRoomInformation> roominfor = roomInformationService.lambdaQuery().eq(AddColdStorageRoomInformation::getRoomCode, each.getRoomCode()).list();
                each.setRoomName(roominfor.get(0).getRoomName());
            }
        }
        model.addAttribute("inspecting",list);
        return "InspectingDisplay";
    }

    @GetMapping("/returntoquerycheckitem")
    public String returnToQueryCheckItem(){
        return "redirect:/querycheckitem";
    }

    //提交审批项目
    @GetMapping("/waitcheckitem")
    public String waitCheckItem(Model model){
        //得到当前用户信息
        User logInformation = (User) SecurityUtils.getSubject().getPrincipal();
        String userName = logInformation.getUserName();

//        List<User> list = loginService.lambdaQuery().eq(User::getUserName, userName).list();
//        System.out.println(list.get(0));
        //获得
        List<TempSaveItems> list = tempSaveService.getAllByOperatorTempSaveItems(userName);
        if (!(list.size()==0)){
            for (TempSaveItems each : list){
                each.setDates(DateAndTimeStamp.timeStampToDate(each.getRegistrationTimeStamp()));
                List<AddColdStorageRoomInformation> roominfor = roomInformationService.lambdaQuery().eq(AddColdStorageRoomInformation::getRoomCode, each.getRoomCode()).list();
                each.setRoomName(roominfor.get(0).getRoomName());
                if (each.getStatus().equals("1")){
                    each.setComment("入库审核中");
                }if (each.getStatus().equals("2")){
                    each.setComment("入库审核拒绝");
                }if (each.getStatus().equals("3")){
                    each.setComment("入库审核通过");
                }if (each.getStatus().equals("4")){
                    each.setComment("取消入库预订");
                }if (each.getStatus().equals("5")){
                    each.setComment("入库成功");
                }if (each.getStatus().equals("6")){
                    each.setComment("出库审核中");
                }if (each.getStatus().equals("7")){
                    each.setComment("出库审核拒绝");
                }if (each.getStatus().equals("8")){
                    each.setComment("出库审核通过");
                }if (each.getStatus().equals("9")){
                    each.setComment("取消出库");
                }if (each.getStatus().equals("10")){
                    each.setComment("出库成功");
                }
            }
            model.addAttribute("inspecting",list);
        }
        return "WaitCheckItem";
    }

    //撤销提交的审批项目
    @GetMapping("/revoke")
    public String revokeItem(String id){

        TempSaveItems tempSaveItems = tempSaveService.selectAllByIdCustom(Integer.parseInt(id));

        if (!tempSaveItems.getStatus().equals("1")){
            //根据暂存表id获得相应货物信息
            //得到货物预约的编号code
            String[] nums = tempSaveItems.getRoomPoint().split(",");
            for (String num:nums){
                //释放库房内部编号表的状态
                itemService.updateStatusByCode(tempSaveItems.getRoomCode()+"_code","1","正常空闲",num);
            }
            //修改总库表，释放可用容量和未用容量
            AddColdStorageRoomInformation room = roomInformationService.getRoomByRoomCode(tempSaveItems.getRoomCode());
            room.setUnusedCapacity(String.valueOf(Integer.parseInt(room.getUnusedCapacity())+Integer.parseInt(tempSaveItems.getVolume())));
            room.setUsedCapacity(String.valueOf(Integer.parseInt(room.getUsedCapacity())-Integer.parseInt(tempSaveItems.getVolume())));
            roomInformationService.updateVolume(room.getUnusedCapacity(),room.getUsedCapacity(),room.getRoomCode());

        }
        //修改暂存表的状态
        tempSaveService.updateStatusById(id,"4");
        return "redirect:/waitcheckitem";
    }
    @GetMapping("/reopen")
    public String reOpen(String id,Model model) throws ParseException {

        //修改暂存表的状态
        tempSaveService.updateStatusById(id,"1");
        //修改暂存表时间戳和编号
        String timeStamps = DateAndTimeStamp.dateToTimeStamp();

        return "redirect:/waitcheckitem";
    }

    //审核通过，用户进行下一步
    @GetMapping("/indetail")
    public String inDetail(String id,Model model){

        TempSaveItems tempSaveItems = tempSaveService.selectAllByIdCustom(Integer.parseInt(id));
        System.out.println("========================");
        System.out.println(tempSaveItems);
        String[] nums = tempSaveItems.getRoomPoint().split(",");
        int len = nums.length;
        //仓位个数
        tempSaveItems.setCubicleNums(len);
        //获得相应库房信息
        AddColdStorageRoomInformation room = roomInformationService.getRoomByRoomCode(tempSaveItems.getRoomCode());
        //总价
        BigDecimal num=new BigDecimal(len);
        BigDecimal dailyCost = new BigDecimal(room.getRoomDailyCost());
        BigDecimal resultSaveCost = num.multiply(dailyCost);
        tempSaveItems.setSaveCost(resultSaveCost.toString());
        tempSaveItems.setAllCost(tempSaveItems.getSendCost());
        model.addAttribute("tempSaveItems",tempSaveItems);
        //转换时间
        tempSaveItems.setDates(DateAndTimeStamp.timeStampToDate(tempSaveItems.getRegistrationTimeStamp()));
        model.addAttribute("room",room);

        return "inDetail";
    }

    //一键处理
    @GetMapping("/quickprocess")
    public String allApproved() throws ParseException {
        User user = (User) SecurityUtils.getSubject().getPrincipal();
        ItemInformation itemInformation = new ItemInformation();
        while(tempSaveService.countByInspectorInteger(user.getUserName())>0) {
            TempSaveItems oneTempSave = tempSaveService.getOneByMinStamp(user.getUserName());
            if (oneTempSave==null){
                break;
            }
            List<AddColdStorageRoomInformation> oneRoomInformation = roomInformationService.lambdaQuery().eq(AddColdStorageRoomInformation::getRoomCode, oneTempSave.getRoomCode()).list();
            String roomCode = oneTempSave.getRoomCode();
            if (Double.parseDouble(oneRoomInformation.get(0).getUnusedCapacity())>=Double.parseDouble(oneTempSave.getItemWeight())){
                //执行入库操作
                //新增入库信息、修改库存容量、记录入库信息
                //将物品入库
                Boolean isSaveI = insertItemInformation(oneTempSave, itemInformation);
                //更新仓库
                Boolean isUpdateR = updateRoomInformation(oneRoomInformation, oneTempSave);
                //执行临时表删除该条数据
                Boolean isDeleteT = tempSaveService.deleteById(oneTempSave.getId());
                oneTempSave.setStatus("1");
            }else {
                //库房不足驳回
                volumeNotEnough(oneTempSave);
            }
            //新增记录
            Boolean isInsertR = insertNewRecord(itemInformation, oneTempSave);

        }
        return "redirect:/querycheckitem";
    }

    //通过
    //@ReWrite
    @GetMapping("/approved")
    public String approved(String id,Model model) throws ParseException {
        System.out.println("tempSaveItem.id      :"+id);
        ItemInformation itemInformation = new ItemInformation();
        TempSaveItems tempSaveItems = tempSaveService.selectAllByIdCustom(Integer.parseInt(id));
        System.out.println("tempSaveItem         :"+tempSaveItems);
        //如果是审核退还的货物信息
        if (tempSaveItems.getStatus().equals("2")){
            List<AddColdStorageRoomInformation> roomInformation = roomInformationService.list();
            model.addAttribute("currentdata",tempSaveItems);
            model.addAttribute("roomInformation",roomInformation);
            return "UpdateItemForStatus";
        }
        //重写审核，此处应跳转新的页面，显示全部货物信息并选择入库具体位置

        //得到总库表相应库房信息
        AddColdStorageRoomInformation room = roomInformationService.getRoomByRoomCode(tempSaveItems.getRoomCode());
        //得到相应库房的内部编号序列
        List<RoomCubicle> roomCubicles = itemService.selectAllCubicle(room.getRoomCode() + "_code");
        //将时间戳转换为时间
        String date = DateAndTimeStamp.timeStampToDate(tempSaveItems.getRegistrationTimeStamp());
        tempSaveItems.setDates(date);
        //开始传送
        model.addAttribute("tempSaveItems",tempSaveItems);
        model.addAttribute("room",room);
        model.addAttribute("roomCubicles", JSON.toJSONString(roomCubicles));
//        List<AddColdStorageRoomInformation> oneRoomInformation = roomInformationService.lambdaQuery().eq(AddColdStorageRoomInformation::getRoomCode, tempSaveItems.getRoomCode()).list();
//        if (Double.parseDouble(oneRoomInformation.get(0).getUnusedCapacity())>=Double.parseDouble(tempSaveItems.getItemWeight())) {
//            //执行入库操作
//            //新增入库信息、修改库存容量、记录入库信息
//            //将物品入库
//            Boolean isSaveI = insertItemInformation(tempSaveItems, itemInformation);
//            //更新仓库
//            Boolean isUpdateR = updateRoomInformation(oneRoomInformation, tempSaveItems);
//            //执行临时表删除该条数据
//            Boolean isDeleteT = tempSaveService.deleteById(tempSaveItems.getId());
//            tempSaveItems.setStatus("1");
//        }else {
//            volumeNotEnough(tempSaveItems);
//        }
//
//        //新增记录
//        itemInformation.setRegistrationTimeStamp(DateAndTimeStamp.dateToTimeStamp());
//        Boolean isInsertR = insertNewRecord(itemInformation, tempSaveItems);
        return "CheckingForCubicles";
    }

    //未通过
    @GetMapping("/unapproved")
    public String unApproved(String id,Model model){

        TempSaveItems tempSaveItems = tempSaveService.selectAllByIdCustom(Integer.parseInt(id));
        //如果是审核退还的货物信息
        if (tempSaveItems.getStatus().equals("2")){
            List<AddColdStorageRoomInformation> roomInformation = roomInformationService.list();
            model.addAttribute("currentdata",tempSaveItems);
            model.addAttribute("roomInformation",roomInformation);
            return "UpdateItemForStatus";
        }
        model.addAttribute("currentdata",tempSaveItems);
        tempSaveItems.setStatus("2");
        return "CheckFailure";
    }

    //未通过的处理
    @GetMapping("/hanlderunApproved")
    public String hanlderUnApproved(TempSaveItems tempSaveItems) throws ParseException {
        ItemInformation itemInformation = new ItemInformation();
        User user = (User) SecurityUtils.getSubject().getPrincipal();
        tempSaveItems.setOperator(user.getUserName());
        itemInformation.setRegistrationTimeStamp(DateAndTimeStamp.dateToTimeStamp());
        //insertNewRecord(itemInformation, tempSaveItems);
        tempSaveService.updateByIdCustom(tempSaveItems);
        return "redirect:/querycheckitem";
    }

    //对于审核退还的进行修改
    @GetMapping("/updateitems")
    public String updateItems(TempSaveItems tempSaveItems) throws ParseException {
        ItemInformation itemInformation = new ItemInformation();

        User user = (User) SecurityUtils.getSubject().getPrincipal();
        tempSaveItems.setOperator(user.getUserName());
        tempSaveItems.setStatus("1");
        itemInformation.setRegistrationTimeStamp(DateAndTimeStamp.dateToTimeStamp());
        //insertNewRecord(itemInformation, tempSaveItems);
        tempSaveService.updateRoomCodeByIdCustom(tempSaveItems);
        return "redirect:/querycheckitem";
    }

    public Boolean insertItemInformation(TempSaveItems oneTempSave,ItemInformation itemInformation) throws ParseException {
        String roomCode = oneTempSave.getRoomCode();
        itemInformation.setItemName(oneTempSave.getItemName());
        itemInformation.setItemWeight(oneTempSave.getItemWeight());
        itemInformation.setInspectRegistrationTimeStamp(oneTempSave.getRegistrationTimeStamp());
        itemInformation.setRegistrationTimeStamp(DateAndTimeStamp.dateToTimeStamp());
        itemInformation.setIdentifier(oneTempSave.getIdentifier());
        itemInformation.setOperator(oneTempSave.getOperator());
        itemInformation.setInspector(oneTempSave.getInspector());
        //执行物品数据库操作
        return itemService.insertByRoomCode(itemInformation,roomCode);
    }

    public Boolean updateRoomInformation(List<AddColdStorageRoomInformation> oneRoomInformation,TempSaveItems oneTempSave){
        String roomCode = oneTempSave.getRoomCode();
        Double roomUnusedCapacity = Double.parseDouble(oneRoomInformation.get(0).getUnusedCapacity());
        Double roomUsedCapacity = Double.parseDouble(oneRoomInformation.get(0).getUsedCapacity());
        Double itemWeight = Double.parseDouble(oneTempSave.getItemWeight());
        String unusedCapacity = String.valueOf(roomUnusedCapacity-itemWeight);
        String usedCapacity = String.valueOf(roomUsedCapacity+itemWeight);
        //执行仓库更新操作
        return roomInformationService.updateVolume(unusedCapacity, usedCapacity, roomCode);
    }

    public void volumeNotEnough(TempSaveItems oneTempSave){
        oneTempSave.setStatus("2");
        oneTempSave.setComment("驳回原因：库房容量不足！");
        String operator = oneTempSave.getOperator();
        String inspector = oneTempSave.getInspector();
        oneTempSave.setOperator(inspector);
        oneTempSave.setInspector(operator);
        tempSaveService.updateByIdCustom(oneTempSave);
    }

    private Boolean insertNewRecord(ItemInformation itemInformation, TempSaveItems oneTempSave){
        String roomCode = oneTempSave.getRoomCode();
        String roomLabelCode = oneTempSave.getRoomLabelCode();
        WarehousingRecord warehousingRecord = new WarehousingRecord();
        warehousingRecord.setItemName(oneTempSave.getItemName());
        warehousingRecord.setItemWeight(oneTempSave.getItemWeight());
        warehousingRecord.setInspectRegistrationTimeStamp(oneTempSave.getRegistrationTimeStamp());
        warehousingRecord.setRegistrationTimeStamp(itemInformation.getRegistrationTimeStamp());
        warehousingRecord.setRoomCode(roomCode);
        warehousingRecord.setRoomLabelCode(roomLabelCode);
        warehousingRecord.setOperator(oneTempSave.getOperator());
        warehousingRecord.setInspector(oneTempSave.getInspector());
        warehousingRecord.setStatus(oneTempSave.getStatus());
        warehousingRecord.setComment(oneTempSave.getComment());
        warehousingRecord.setIdentifier(GenerateIdentifier.generateIdentifier(roomLabelCode,roomCode));
        //执行插入报表工作
        return warehousingRecordService.save(warehousingRecord);
    }

    private Boolean insertNewRecord(ItemInformation itemInformation,String roomCode,RoomLabel roomLabel){
        String roomLabelCode = roomLabel.getRoomLabelCode();
        WarehousingRecord warehousingRecord = new WarehousingRecord();
        warehousingRecord.setItemName(itemInformation.getItemName());
        warehousingRecord.setItemWeight(itemInformation.getItemWeight());
        warehousingRecord.setInspectRegistrationTimeStamp(itemInformation.getRegistrationTimeStamp());
        warehousingRecord.setRegistrationTimeStamp(itemInformation.getRegistrationTimeStamp());
        warehousingRecord.setRoomCode(roomCode);
        warehousingRecord.setRoomLabelCode(roomLabelCode);
        warehousingRecord.setOperator(itemInformation.getOperator());
        warehousingRecord.setInspector(itemInformation.getInspector());
        warehousingRecord.setStatus("1");
        warehousingRecord.setComment("");
        warehousingRecord.setIdentifier(itemInformation.getIdentifier());
        //执行插入报表工作
        return warehousingRecordService.save(warehousingRecord);
    }


    //转仓
    @GetMapping("transferitem")
    public String transferItem(String id, String room, Model model){
        //得到库房编号
        String roomCode = roomInformationService.selectById(Integer.parseInt(room));
        //得到货物相关信息
        ItemInformation itemInformation = itemService.selectById(roomCode, id);
        //得到库房所有信息
        List<AddColdStorageRoomInformation> roomInformation = SplicingNameCode(roomCode,itemInformation);
        if (roomInformation == null){
            model.addAttribute("zeroRoom","无多余空闲库房！");
            return "Transfer";
        }
        //传递库房信息
        model.addAttribute("roomInformation",roomInformation);
        //传递货物信息
        model.addAttribute("currentdata",itemInformation);
        //传递库房编号和货物编号
        model.addAttribute("id",id);
        model.addAttribute("room",roomCode);
        return "Transfer";
    }
    //进入新库房
    @GetMapping("/itemtransfer")
    public String itemToNewRoom(Model model, @Valid ItemInformation itemInformation, BindingResult bindingResult,String oldRoomCode) throws ParseException {
        HashMap<String, String> errorMap = new HashMap<>();
        if (bindingResult.hasErrors()){
            //得到货物相关信息
            ItemInformation item = itemService.selectById(oldRoomCode, String.valueOf(itemInformation.getId()));
            //得到库房所有信息
            List<AddColdStorageRoomInformation> roomInformation = SplicingNameCode(oldRoomCode,itemInformation);
            if (roomInformation == null){
                model.addAttribute("zeroRoom","无多余空闲库房！");
                return "Transfer";
            }
            errorMap = VerifyData.validData(errorMap, bindingResult);
            model.addAttribute("errorMap",errorMap);
            model.addAttribute("currentdata",item);
            //传递库房信息
            model.addAttribute("roomInformation",roomInformation);
            return "Transfer";
        }

        //插入相应库房
        Boolean isInsert = itemService.insertByRoomCode(itemInformation, itemInformation.getStorageRoom().getRoomCode());
        //之前的库房删除
        Boolean isDelete = itemService.deleteByIdCustom(oldRoomCode, String.valueOf(itemInformation.getId()));

        //修改库房总表
        //旧的库房信息
        List<AddColdStorageRoomInformation> oldRoomInformation = roomInformationService.lambdaQuery().eq(AddColdStorageRoomInformation::getRoomCode, oldRoomCode).list();
        //就的已用容量-，未用容量+
        Double oldUse = Double.parseDouble(oldRoomInformation.get(0).getUsedCapacity()) - Double.parseDouble(itemInformation.getItemWeight());
        Double oldUnUse = Double.parseDouble(oldRoomInformation.get(0).getUnusedCapacity()) + Double.parseDouble(itemInformation.getItemWeight());
        roomInformationService.updateVolume(String.valueOf(oldUnUse),String.valueOf(oldUse),oldRoomCode);
        //新的库房信息
        List<AddColdStorageRoomInformation> newRoomInformation = roomInformationService.lambdaQuery().eq(AddColdStorageRoomInformation::getRoomCode, itemInformation.getStorageRoom().getRoomCode()).list();
        //新的已用容量+，未用容量-
        Double newUse = Double.parseDouble(newRoomInformation.get(0).getUsedCapacity()) + Double.parseDouble(itemInformation.getItemWeight());
        Double newUnUse = Double.parseDouble(newRoomInformation.get(0).getUnusedCapacity()) - Double.parseDouble(itemInformation.getItemWeight());
        roomInformationService.updateVolume(String.valueOf(newUse),String.valueOf(newUnUse),itemInformation.getStorageRoom().getRoomCode());
        //存入转库表
        Transfer transfer = new Transfer();
        transfer.setOldRoom(oldRoomCode);
        transfer.setNewRoom(itemInformation.getStorageRoom().getRoomCode());
        transfer.setIdentifier(itemInformation.getIdentifier());
        transfer.setTransferTimestamp(DateAndTimeStamp.dateToTimeStamp());

        transferService.insertTransfer(transfer);
        return "redirect:/excoldstorageitem";
    }

    //合并名称-编号-温度
    public List<AddColdStorageRoomInformation> SplicingNameCode(String room,ItemInformation itemInformation){
        List<AddColdStorageRoomInformation> roomInformation = roomInformationService.list();
        List<AddColdStorageRoomInformation> roomInformations = new ArrayList<>();
        String roomName,roomCode,unusedCapacity;
        for (AddColdStorageRoomInformation list:roomInformation){
            if (Double.parseDouble(list.getUnusedCapacity())>=Double.parseDouble(itemInformation.getItemWeight()) && !room.equals(list.getRoomCode())) {
                roomName = list.getRoomName();
                roomCode = list.getRoomCode();
                unusedCapacity = list.getUnusedCapacity();
                list.setRoomName(roomName + "-" + roomCode + "-" + unusedCapacity);
                roomInformations.add(list);
            }
        }
        if (roomInformations.size()==0){
            return null;
        }
        return roomInformations;
    }

    //请求库房隔间数据
    @PostMapping("/cubiclesdata")
    @ResponseBody
    public JSONArray cubiclesData(String roomCode){
        JSONArray jsonArray = new JSONArray();
        List<RoomCubicle> roomCubicles = itemService.selectAllCubicle(roomCode + "_code");
        System.out.println(roomCubicles);
        for (RoomCubicle roomCubicle:roomCubicles){
            JSONObject jsonObject = (JSONObject) JSONObject.toJSON(roomCubicle);
            jsonArray.add(jsonObject);
        }
        System.out.println(jsonArray);
        return jsonArray;
    }
    @PostMapping("/checkingsuccess")
    @ResponseBody
    public Boolean checkingSuccess(String tempId,String roomId,String itemWeight,String[] nums){
        /*
        * 修改暂存表，库编号表、总表
        * */
        //数据库填写重量、修改状态和入库编号
        //状态为3说明通过审核，发起预约
        Boolean isTemp = tempSaveService.updateStatusBytempId(tempId, itemWeight, StringUtils.join(Arrays.asList(nums), ','), "3");
        //锁定隔间 status=3
        for (String num:nums){
            itemService.updateStatusByCode(roomId+"_code","3","已预约",num);
        }
        //获得temp相应数据
        TempSaveItems tempSaveItems = tempSaveService.selectAllByIdCustom(Integer.parseInt(tempId));
        //修改总表可用容量和未用容量
        AddColdStorageRoomInformation room = roomInformationService.getRoomByRoomCode(roomId);
        room.setUnusedCapacity(String.valueOf(Integer.parseInt(room.getUnusedCapacity())-Integer.parseInt(tempSaveItems.getVolume())));
        room.setUsedCapacity(String.valueOf(Integer.parseInt(room.getUsedCapacity())+Integer.parseInt(tempSaveItems.getVolume())));
        Boolean isRoom = roomInformationService.updateVolume(room.getUnusedCapacity(), room.getUsedCapacity(), roomId);

        return isTemp&&isRoom;
    }

    @PostMapping("/payment")
    @ResponseBody
    public Boolean payment(Integer id) throws ParseException {
        /*
         * 修改暂存表，库编号表、总表
         * */
        //数据库填写重量、修改状态和入库编号

        //获得编号对应的暂存表
        TempSaveItems tempSaveItems = tempSaveService.selectAllByIdCustom(id);
        //转存货物表
        ItemInformation itemInformation = new ItemInformation();
        itemInformation.setItemName(tempSaveItems.getItemName());
        itemInformation.setItemWeight(tempSaveItems.getItemWeight());
        itemInformation.setItemVolume(tempSaveItems.getVolume());
        itemInformation.setInspectRegistrationTimeStamp(tempSaveItems.getRegistrationTimeStamp());
        itemInformation.setRegistrationTimeStamp(DateAndTimeStamp.dateToTimeStamp());
        itemInformation.setIdentifier(tempSaveItems.getIdentifier());
        itemInformation.setOperator(tempSaveItems.getOperator());
        itemInformation.setInspector(tempSaveItems.getInspector());
        itemInformation.setRoomPoint(tempSaveItems.getRoomPoint());
        itemInformation.setTempId(tempSaveItems.getId());
        itemService.insertByRoomCode(itemInformation,tempSaveItems.getRoomCode());

        String[] cubicles = tempSaveItems.getRoomPoint().split(",");
        //获得刚刚入库货物的编号
        ItemInformation item = itemService.selectByTempId(tempSaveItems.getRoomCode(), String.valueOf(tempSaveItems.getId()));
        for (String cubicle:cubicles){
            //修改库房对应的编号表
            itemService.updateRoomByCode(tempSaveItems.getRoomCode()+"_code","4","存在货物",cubicle,String.valueOf(item.getId()));
        }
        //状态为1表示请求审核（等待审核）
        //状态为2拒绝入库（等待用户操作）
        //状态为3表示通过审核（等待用户操作）
        //状态为4表示取消预约
        //状态为5说明已入库，结束入库流程

        //状态为6表示请求出库（等待审核）
        //状态为7表示拒绝出库（等待用户）
        //状态为8表示同意出库（等待用户付款）
        //状态为9表示取消出库
        //状态为10表示出库成功，结束出库流程
        //这个是temp表
        Boolean isUpdate = tempSaveService.updateStatusById(String.valueOf(id), "5");


        return isUpdate;
    }


}
