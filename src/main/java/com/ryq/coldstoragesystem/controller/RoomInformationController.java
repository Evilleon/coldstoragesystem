package com.ryq.coldstoragesystem.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ryq.coldstoragesystem.bean.AddColdStorageRoomInformation;
import com.ryq.coldstoragesystem.bean.RoomLabel;
import com.ryq.coldstoragesystem.bean.RoomCubicle;
import com.ryq.coldstoragesystem.service.LabelService;
import com.ryq.coldstoragesystem.service.RoomInformationService;
import com.ryq.coldstoragesystem.utils.CubicleNumber;
import com.ryq.coldstoragesystem.utils.VerifyData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Controller
public class RoomInformationController {

    @Autowired
    RoomInformationService roomInformationService;

    @Autowired
    LabelService labelService;

    /**
     * 新增仓库信息(前置设置)
     * Bean: AddColdStorageRoomInformation
     * @param model
     * @return
     */
    @GetMapping("/addroominformation")
    public String addRoomInformation(Model model, AddColdStorageRoomInformation addColdStorageRoomInformation){
        Integer mid = 0,maxCode = 0;
        List<String> roomCodeListS = roomInformationService.getRoomCode();
        List<Integer> roomCodeListI = new ArrayList<>();
        String currentCode;
        System.out.println(roomCodeListS.size());
        if (roomCodeListS.size()!=0){
            for(String items:roomCodeListS){
                mid = Integer.valueOf(items.substring(3));
                if(maxCode < mid){
                    maxCode = mid;
                }
            }
            currentCode = "0"+(maxCode+1);
        }else{
            currentCode = "0500000"+1;
        }
        addColdStorageRoomInformation.setRoomCode("sjz"+currentCode);
        model.addAttribute("currentdata",addColdStorageRoomInformation);
        List<RoomLabel> labelInformation = labelService.list();
        model.addAttribute("labelinformation",labelInformation);
        return "AddRoomInformation";
    }

    /**
     * 保存冷库仓室信息
     * @param model
     * @param addColdStorageRoomInformation
     * @return
     */
    @GetMapping("/saveroominformation")
    public String saveRoomInformation(Model model, @Valid AddColdStorageRoomInformation addColdStorageRoomInformation, BindingResult bindingResult){
        HashMap<String, String> errorMap = new HashMap<>();
        System.out.println(addColdStorageRoomInformation);
        //判断数据合法性
        if(bindingResult.hasErrors()){
            errorMap = VerifyData.validData(errorMap,bindingResult);
            model.addAttribute("errorMap",errorMap);
            model.addAttribute("currentdata",addColdStorageRoomInformation);
            System.out.println(addColdStorageRoomInformation);
            return "AddRoomInformation";
        }
        else {
            //参数填充
            addColdStorageRoomInformation.setUsedCapacity("0");
            RoomLabel label = labelService.getLabelByCode(addColdStorageRoomInformation.getRoomLabelCode());
            addColdStorageRoomInformation.setRoomLabelName(label.getRoomLabelName());
            addColdStorageRoomInformation.setUnusedCapacity(addColdStorageRoomInformation.getRoomVolume());
            addColdStorageRoomInformation.setCurrentTemperature(addColdStorageRoomInformation.getRoomTemperature());
            addColdStorageRoomInformation.setRoomKey("0");
            roomInformationService.addRoomInformation(addColdStorageRoomInformation);
            System.out.println(addColdStorageRoomInformation.getLng());
            //新建仓库表
            Boolean saveTable = roomInformationService.creatTables(addColdStorageRoomInformation.getRoomCode());
            //根据长宽高新建仓库代码表
            roomInformationService.createCodeTables(addColdStorageRoomInformation.getRoomCode()+"_code");
            //更新仓库代码表，将长宽
            int length = addColdStorageRoomInformation.getLength();
            int width = addColdStorageRoomInformation.getWidth();
            int height = addColdStorageRoomInformation.getHeight();
            //获得长宽编号对应的的每个编号
            List<String> strCodes = CubicleNumber.generateCode(length, width);
            List<RoomCubicle> roomCubicles = new ArrayList<>();
            for (String strCode:strCodes){
                RoomCubicle roomCubicle = new RoomCubicle();
                roomCubicle.setCode(strCode);
                //1代表正常空闲
                //2代表正常不空闲
                //3代表不正常
                //4代表锁定
                roomCubicle.setStatus("1");
                roomCubicle.setComment("正常空闲");
                roomCubicle.setVolume(String.valueOf(height));
                roomCubicles.add(roomCubicle);
                //插入数据库
            }
            roomInformationService.insertCodeTables(roomCubicles,addColdStorageRoomInformation.getRoomCode()+"_code");
            model.addAttribute("msg",saveTable);
            return "redirect:/roominformationdisplay";
        }
    }


    /**
     * 无条件展示仓库信息
     */
    @GetMapping("/roominformationdisplay")
    public String roomDisplay(Model model){
        //AddColdStorageRoomInformation allRoomInformation = roomInformationService.getAllRoomInformation();
        List<AddColdStorageRoomInformation> allRoomInformation = roomInformationService.list();
        model.addAttribute("allRoomInformation",allRoomInformation);
        return "RoomInformationDisplay";
    }

    //去修改界面
    @GetMapping("/reviseroom")
    public String reviseRoom(Model model,String id){
        AddColdStorageRoomInformation roomInformation = roomInformationService.getAllById(Integer.valueOf(id));
        model.addAttribute("currentdata",roomInformation);

        return "ReviseRoom";
    }

    @GetMapping("/reviseinroom")
    public String reviseInRoom(AddColdStorageRoomInformation addColdStorageRoomInformation){
        addColdStorageRoomInformation.setCurrentTemperature(addColdStorageRoomInformation.getRoomTemperature());

        roomInformationService.updateByid(addColdStorageRoomInformation);

        return "redirect:/roominformationdisplay";
    }

    //搜索
    @GetMapping("/searchroom")
    public String searchRoom(Model model,String searchType,String searchWord){

        List<AddColdStorageRoomInformation> addColdStorageRoomInformations;

        if (searchType.equals("roomName")){
            //根据名称取
            addColdStorageRoomInformations = roomInformationService.selectRoomByRoomName(searchWord);
        }else if(searchType.equals("roomCode")){
            //根据编号取
            addColdStorageRoomInformations = roomInformationService.selectRoomByRoomCode(searchWord);
        }else if(searchType.equals("specialTemperature")){
            //根据温度取
            addColdStorageRoomInformations = roomInformationService.selectRoomByTemperature(searchWord);
        }else{
            //根据费用取
            addColdStorageRoomInformations = roomInformationService.selectRoomByCost(searchWord);
        }

        model.addAttribute("allRoomInformation",addColdStorageRoomInformations);
        return "RoomInformationDisplay";
    }

    @PostMapping("/roommsg")
    @ResponseBody
    public JSONArray roomMsg(){
        JSONArray jsonArray = new JSONArray();
        List<AddColdStorageRoomInformation> allRoomInformation = roomInformationService.getAllRoomInformation();
        for (AddColdStorageRoomInformation room:allRoomInformation){
            JSONObject jsonObject = (JSONObject) JSONObject.toJSON(room);
            jsonArray.add(jsonObject);
        }
        System.out.println(jsonArray);
        return jsonArray;
    }

    @PostMapping("/selectbylabelcode")
    @ResponseBody
    public JSONArray selectByLabelCode(String roomLabelCode){
        JSONArray jsonArray = new JSONArray();
        List<AddColdStorageRoomInformation> rooms = roomInformationService.selectRoomByLabelCode(roomLabelCode);
        if (rooms.size()==0){
            return null;
        }
        for (AddColdStorageRoomInformation room:rooms){
            JSONObject jsonObject = (JSONObject) JSONObject.toJSON(room);
            jsonArray.add(jsonObject);
        }
        System.out.println(jsonArray);
        return jsonArray;
    }

    @PostMapping("/getroombyroomcode")
    @ResponseBody
    public JSONArray getRoomByRoomCode(String roomCode){
        JSONArray jsonArray = new JSONArray();
        AddColdStorageRoomInformation room = roomInformationService.getRoomByRoomCode(roomCode);
        JSONObject jsonObject = (JSONObject) JSONObject.toJSON(room);
        jsonArray.add(jsonObject);
        System.out.println(jsonArray);
        return jsonArray;
    }
}
