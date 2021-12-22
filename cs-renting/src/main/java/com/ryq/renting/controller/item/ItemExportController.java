package com.ryq.renting.controller.item;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ryq.coldstoragesystem.bean.*;
import com.ryq.coldstoragesystem.service.*;
import com.ryq.coldstoragesystem.utils.DateAndTimeStamp;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
public class ItemExportController {

    @Autowired
    RoomInformationService roomInformationService;

    @Autowired
    ItemService itemService;

    @Autowired
    CustomerService customerService;

    @Autowired
    TransportService transportService;

    @Autowired
    ExportListService exportListService;

    @Autowired
    DeliveryRecordService deliveryRecordService;

    @Autowired
    TempSaveService tempSaveService;

    /**
     * 出库设置
     */

    @GetMapping("/excoldstorageitem")
    public String ExColdStorage(Model model){
//        Integer count = roomInformationService.lambdaQuery().count();
//        model.addAttribute("count",count);
        //查询temp表状态为5的货物，操作员为当前用户
        User user = (User)SecurityUtils.getSubject().getPrincipal();
        List<TempSaveItems> tempSaveItems = tempSaveService.selectAllByStatus(user.getUserName(), "5");
        for (TempSaveItems tempSaveItem:tempSaveItems){
            String roomCode = tempSaveItem.getRoomCode();
            AddColdStorageRoomInformation room = roomInformationService.getRoomByRoomCode(roomCode);
            tempSaveItem.setRoomName(room.getRoomName());
            tempSaveItem.setDates(DateAndTimeStamp.timeStampToDate(tempSaveItem.getRegistrationTimeStamp()));
        }
        model.addAttribute("tempSaveItems",tempSaveItems);
        return "ExColdStorageItem";

    }

    @GetMapping("/returntoexcoldstorageitem")
    public String returnToexColdStorageItem(){
        return "redirect:/excoldstorageitem";
    }
    //展示库存信息
    @RequestMapping("/displayitem")
    @ResponseBody
    public JSONArray DisplayItem(String id){
        String roomCode = roomInformationService.selectById(Integer.parseInt(id));
        List<ItemInformation> itemInformations = itemService.selectByTableName(roomCode);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        JSONArray jsonArray = new JSONArray();
        //JSONArray jsonArrayTest = new JSONArray();
        for (int each=0;each<itemInformations.size();each++){
            String time_Date = DateAndTimeStamp.timeStampToDate(itemInformations.get(each).getRegistrationTimeStamp());
            itemInformations.get(each).setRegistrationTimeStamp(time_Date);
            //不再使用json字符串传输数据
            //String each_item = JSON.toJSONString(itemInformations.get(each));
            JSONObject jsonObject = (JSONObject) JSONObject.toJSON(itemInformations.get(each));
            jsonArray.add(jsonObject);
            //jsonArrayTest.add(each_item);
        }
        System.out.println(jsonArray);
        System.out.println("========================");
        //System.out.println(jsonArrayTest);
        return jsonArray;
    }

    //可以没有？
    //修改
    @GetMapping("/edititem")
    public String editItem(String id){
        System.out.println(id);
        return "ExColdStorageItem";
    }

    @PostMapping("/checkingexportitem")
    @ResponseBody
    public String checkingExportItem(String id){
        tempSaveService.updateStatusById(id,"6");
        return "redirect:/waitcheckitem";
    }


    //查询是否有要出库审批的项目
    @GetMapping("/querycheckexitem")
    public String QueryCheckExItem(Model model){
        //得到当前用户信息
        User logInformation = (User) SecurityUtils.getSubject().getPrincipal();
        String userName = logInformation.getUserName();

//        List<User> list = loginService.lambdaQuery().eq(User::getUserName, userName).list();
//        System.out.println(list.get(0));
        //获得
        List<TempSaveItems> list = tempSaveService.getAllByInspectorTempSaveItems(userName,"6");
        if (!(list.size()==0)){
            for (TempSaveItems each : list){
                ItemInformation item = itemService.selectByTempId(each.getRoomCode(), String.valueOf(each.getId()));
                each.setDates(DateAndTimeStamp.timeStampToDate(item.getRegistrationTimeStamp()));
                List<AddColdStorageRoomInformation> roominfor = roomInformationService.lambdaQuery().eq(AddColdStorageRoomInformation::getRoomCode, each.getRoomCode()).list();
                each.setRoomName(roominfor.get(0).getRoomName());
                each.setVolume(item.getItemVolume());
                System.out.println(item.getItemVolume());
            }
        }

        model.addAttribute("inspecting",list);
        return "InspectingExDisplay";
    }
    @GetMapping("/approvedex")
    public String approvedEx(String id, Model model){
        TempSaveItems tempSaveItems = tempSaveService.selectAllByIdCustom(Integer.parseInt(id));
        tempSaveItems.setDates(DateAndTimeStamp.timeStampToDate(tempSaveItems.getRegistrationTimeStamp()));

        ExportList exportList = exportListService.selectByTempId(id);

        ItemInformation item = itemService.selectByTempId(tempSaveItems.getRoomCode(), String.valueOf(tempSaveItems.getId()));
        item.setDate(DateAndTimeStamp.timeStampToDate(item.getRegistrationTimeStamp()));

        AddColdStorageRoomInformation room = roomInformationService.getRoomByRoomCode(tempSaveItems.getRoomCode());
        tempSaveItems.setRoomName(room.getRoomName());

        model.addAttribute("tempSaveItems",tempSaveItems);
        model.addAttribute("item",item);
        model.addAttribute("room",room);
        model.addAttribute("exportList",exportList);
        return "CheckingExItem";
    }
    //同意出库后整理库房
    @PostMapping("/approvedexsuccess")
    @ResponseBody
    public Boolean approvedExSuccess(String id){
        //获得temp表相应数据
        TempSaveItems tempSaveItems = tempSaveService.selectAllByIdCustom(Integer.parseInt(id));

        //修改temp状态为8

        return tempSaveService.updateStatusById(String.valueOf(tempSaveItems.getId()), "8");

    }

    //exdetail
    @PostMapping("/exdetail")
    @ResponseBody
    public Boolean exDetail(String id){
        //获得temp表相应数据
        TempSaveItems tempSaveItems = tempSaveService.selectAllByIdCustom(Integer.parseInt(id));
        //修改总库
        AddColdStorageRoomInformation room = roomInformationService.getRoomByRoomCode(tempSaveItems.getRoomCode());
        room.setUnusedCapacity(String.valueOf(Integer.parseInt(room.getUnusedCapacity())+Integer.parseInt(tempSaveItems.getVolume())));
        room.setUsedCapacity(String.valueOf(Integer.parseInt(room.getUsedCapacity())-Integer.parseInt(tempSaveItems.getVolume())));
        roomInformationService.updateVolume(room.getUnusedCapacity(),room.getUsedCapacity(),room.getRoomCode());
        //修改库房表
        ItemInformation item = itemService.selectByTempId(room.getRoomCode(), id);
        itemService.deleteByIdCustom(room.getRoomCode(),String.valueOf(item.getId()));
        //修改库房编号表
        String[] nums = item.getRoomPoint().split(",");
        for (String num:nums){
            itemService.updateStatusByCode(room.getRoomCode()+"_code","1","正常空闲",num);
        }
        //修改temp状态为8
        Boolean isUpdate = tempSaveService.updateStatusById(String.valueOf(tempSaveItems.getId()), "10");
        return isUpdate;
    }

    @PostMapping("/searchexportlist")
    @ResponseBody
    public JSONArray searchExportList(String id){
        JSONArray jsonArray = new JSONArray();
        ExportList exportList = exportListService.selectByTempId(id);
        JSONObject jsonObject = (JSONObject) JSONObject.toJSON(exportList);
        jsonArray.add(jsonObject);
        System.out.println(jsonArray);
        return jsonArray;
    }

    @GetMapping("returntoqueryex")
    public String returnToQueryEx(){
        return "redirect:/querycheckexitem";
    }
    //出库
    @GetMapping("/exportitem")
    public String exportItem(String id,Model model){
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
        //此时状态为5
        TempSaveItems tempSaveItems = tempSaveService.selectAllByIdCustom(Integer.parseInt(id));
        tempSaveItems.setDates(DateAndTimeStamp.timeStampToDate(tempSaveItems.getRegistrationTimeStamp()));
        model.addAttribute("tempSaveItems",tempSaveItems);
        ItemInformation itemInformations = itemService.selectByTempId(tempSaveItems.getRoomCode(), String.valueOf(tempSaveItems.getId()));
        itemInformations.setDate(DateAndTimeStamp.timeStampToDate(itemInformations.getRegistrationTimeStamp()));
        AddColdStorageRoomInformation room = roomInformationService.getRoomByRoomCode(tempSaveItems.getRoomCode());
        model.addAttribute("room",room);
        model.addAttribute("itemInformations",itemInformations);
        //调取收货人
        User user = (User) SecurityUtils.getSubject().getPrincipal();
        List<Customer> customers = customerService.selectAllCustomerCustom(user.getUserName());
        model.addAttribute("customers",customers);
        //计算存储天数和花费
        String[] cubicles = tempSaveItems.getRoomPoint().split(",");
        BigDecimal len = new BigDecimal(cubicles.length);
        BigDecimal baseCost = new BigDecimal(room.getRoomDailyCost());
        BigDecimal cost = len.multiply(baseCost);
        model.addAttribute("cost",cost.toString());
//        tempSaveService.updateStatusById(id,"6");
//        Integer count = roomInformationService.lambdaQuery().count();
//        model.addAttribute("count",count);
//        //库房编号
//        String roomCode = roomInformationService.selectById(Integer.parseInt(room));
//        //库房价格
//        String cost = roomInformationService.selectCostById(Integer.parseInt(room));
//        ItemInformation itemInformations = itemService.selectById(roomCode,id);
//        User user = (User) SecurityUtils.getSubject().getPrincipal();
//        List<Customer> customers = customerService.selectAllCustomerCustom(user.getUserName());
//        itemInformations.setDate(DateAndTimeStamp.timeStampToDateYMD(itemInformations.getRegistrationTimeStamp()));
//        //因为接下来要操作的人是当前用户，所以操作员应该是当前用户
//        itemInformations.setOperator(user.getUserName());
//        model.addAttribute("roomCode",roomCode);
//        model.addAttribute("id",id);
//        model.addAttribute("itemInformations",itemInformations);
//        model.addAttribute("cost",cost);
//        model.addAttribute("customers",customers);
        return "ExportDetail";
    }

    @PostMapping("/searchminfee")
    @ResponseBody
    public JSONArray searchMinFee(String weight){
        JSONArray jsonArray = new JSONArray();
        DecimalFormat decimalFormat=new DecimalFormat(".00");
        System.out.println("weight:"+weight);
        String w=decimalFormat.format(Double.parseDouble(weight));
        List<Transport> transports = transportService.searchMinFee(w);
        for (Transport transport:transports){
            JSONObject jsonObject = (JSONObject) JSONObject.toJSON(transport);
            jsonArray.add(jsonObject);
        }

        System.out.println(jsonArray);

        return jsonArray;
    }

    @PostMapping("/handleexport")
    @ResponseBody
    public Boolean handleExport(ExportList exportList,String id,String roomCode) throws ParseException {

        System.out.println(exportList);
        System.out.println("id"+id);
        System.out.println("roomCode"+roomCode);

        //提交审核
        exportList.setTempId(id);
        Boolean isInExport = exportListService.insertExportList(exportList);
        //修改temp表的状态为6
        Boolean isUpdate = tempSaveService.updateStatusById(id, "6");
        return isInExport && isUpdate;
        //获得当前登录用户名
//        User user = (User) SecurityUtils.getSubject().getPrincipal();
//        exportList.setOperator(user.getUserName());
//        Boolean isInsertExport = exportListService.insertExportList(exportList);
//
//        ItemInformation itemInformation = itemService.selectById(roomCode, id);
//        DecimalFormat decimalFormat = new DecimalFormat(".00");
//
//        DeliveryRecord deliveryRecord = new DeliveryRecord();
//
//
//
//        int flag=0;
//        boolean isInsertCustomer = false;
//        //如果遇到新客户，需要插入
//        //根据姓名寻找相同客户，如果姓名、联系方式、地址都相同，才算相同
//        List<Customer> customers = customerService.selectAllByName(exportList.getConsignee());
//        if (customers.size()!=0){
//            for (Customer c:customers){
//                if (exportList.getConsignee().equals(c.getCustomerName())&&exportList.getContactInformation().equals(c.getCustomerContact())&&exportList.getReceiveAddress().equals(c.getAddress())){
//                    flag=1;
//                    isInsertCustomer = true;
//                    break;
//                }
//            }
//        }
//        if (flag==0){
//            Customer customer = new Customer();
//            customer.setCustomerName(exportList.getConsignee());
//            customer.setCustomerContact(exportList.getContactInformation());
//            customer.setAddress(exportList.getReceiveAddress());
//            customer.setCreater(user.getUserName());
//            customer.setLat(exportList.getLat());
//            customer.setLng(exportList.getLng());
//            isInsertCustomer = customerService.insertCustomer(customer);
//        }
//
//        //出库表
//        //出库表的操作员应该是当前用户，审查员是入库的审查员，应该增加一个字段就是入库员，就是 是谁入库的
//        deliveryRecord.setItemName(exportList.getItemName());
//        deliveryRecord.setItemWeight(exportList.getItemWeight());
//        deliveryRecord.setRoomCode(roomCode);
//        deliveryRecord.setInRegistrationTimeStamp(itemInformation.getRegistrationTimeStamp());
//        deliveryRecord.setExRegistrationTimeStamp(DateAndTimeStamp.preDateToTimeStamp(exportList.getExportDate()));
//        deliveryRecord.setOperator(user.getUserName());
//        deliveryRecord.setInspector(itemInformation.getInspector());
//        //1为等待出库
//        //2为出库中
//        //3为配送中
//        //4为已接收
//        String[] splitInDate = exportList.getWarehousingDate().split("/");
//        String inDate = splitInDate[0]+"/"+splitInDate[1]+"/"+splitInDate[2];;
//        Integer i=inDate.compareTo(exportList.getExportDate());
//        if (i<0){//参大于左
//            deliveryRecord.setStatus("1");
//        }else {
//            deliveryRecord.setStatus("2");
//        }
//
//        deliveryRecord.setIdentifier(itemInformation.getIdentifier());
//        //存入出库表中
//        Boolean isInsertDeliveryRecord = deliveryRecordService.insertDeliveryRecord(deliveryRecord);
//
//        Boolean isupdateOrDelete;
//        //判断是否全部出库
//        //全部出库
//        if (decimalFormat.format(Double.parseDouble(exportList.getItemWeight())).equals(decimalFormat.format(Double.parseDouble(itemInformation.getItemWeight())))){
//
//            //从库房中删除
//             isupdateOrDelete = itemService.deleteByIdCustom(roomCode, id);
//        }
//        //部分出库
//        else{
//            //修改库存表
//            isupdateOrDelete = itemService.updateById(roomCode,id,String.valueOf(Double.parseDouble(itemInformation.getItemWeight())-Double.parseDouble(exportList.getItemWeight())));
//        }
//        //修改总库表的可用容量,以及标签字段
//        AddColdStorageRoomInformation roomByRoomCode = roomInformationService.getRoomByRoomCode(roomCode);
//
//        Double usedCapacity = Double.parseDouble(roomByRoomCode.getUsedCapacity());
//        Double unusedCapacity = Double.parseDouble(roomByRoomCode.getUnusedCapacity());
//        usedCapacity -= Double.parseDouble(exportList.getItemWeight());
//        unusedCapacity += Double.parseDouble(exportList.getItemWeight());
//        if (usedCapacity==0){
//            roomByRoomCode.setRoomLabelCode("0");
//            roomByRoomCode.setRoomLabelName("0");
//        }
//
//        roomInformationService.updateVolumeAndLabel(String.valueOf(unusedCapacity),String.valueOf(usedCapacity),roomByRoomCode.getRoomLabelName(),roomByRoomCode.getRoomLabelCode(),roomCode);
//        return isInsertExport && isInsertDeliveryRecord && isupdateOrDelete && isInsertCustomer;
    }


    //根据登录用户取出相应出库
    @GetMapping("displayexportlists")
    public String displayExportLists(Model model) throws ParseException {
        User user = (User) SecurityUtils.getSubject().getPrincipal();
        List<ExportList> exportLists = exportListService.selectAllByUsername(user.getUserName());
        List<ExportList> newExprotLists = new ArrayList<>();
        //1为等待出库
        //2为出库中
        //3为配送中
        //4为已到达运送地点
        //5为已接收
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");//设置日期格式
        String date = df.format(new Date());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date time = simpleDateFormat.parse(date);
        for (ExportList eachExport:exportLists){
//            if (eachExport.getStatus().equals("1")){
//                eachExport.setStatusText("等待出库");
//                continue;
//            }
//            if (eachExport.getStatus().equals("2")){
//                eachExport.setStatusText("出库中");
//                continue;
//            }
            //如果已经签收，就没必要再进行状态判断了
            if (eachExport.getStatus().equals("5")){
                eachExport.setStatusText("已签收");
                continue;
            }
            boolean isExAfterCurrent = simpleDateFormat.parse(eachExport.getExportDate()).after(time);
            if (isExAfterCurrent){
                eachExport.setStatus("1");
                eachExport.setStatusText("等待出库");
                newExprotLists.add(eachExport);
            }else{
                if (time.getTime()==simpleDateFormat.parse(eachExport.getExportDate()).getTime()){
                    eachExport.setStatus("2");
                    eachExport.setStatusText("出库中");
                    newExprotLists.add(eachExport);
                }else{
                    BigDecimal currentDate = new BigDecimal(simpleDateFormat.parse(eachExport.getExportDate()).getTime());
                    BigDecimal exDate = new BigDecimal(time.getTime());
                    BigDecimal sub = new BigDecimal(3*86400000);
                    if (exDate.subtract(currentDate).compareTo(sub)>0){
                        eachExport.setStatus("4");
                        eachExport.setStatusText("已到达运送地点");
                        newExprotLists.add(eachExport);
                    }else{
                        eachExport.setStatus("3");
                        eachExport.setStatusText("配送中");
                        newExprotLists.add(eachExport);
                    }
                }
            }
        }
        //更新一下状态
        for(ExportList each:newExprotLists){
            exportListService.updateStatusById(each);
        }

        model.addAttribute("exportLists",exportLists);
        return "DisplayExportLists";
    }


    //根据id查询出相应数据并显示
    @GetMapping("/todetail")
    public String displayExportDetails(Model model,String id){

        ExportList exportList = exportListService.selectAllById(Integer.valueOf(id));

        model.addAttribute("exportList",exportList);

        return "DisplayExportDetails";
    }

    //签收
    @GetMapping("/signfor")
    public String signFor(String id){

        ExportList exportList = new ExportList();
        exportList.setId(Integer.valueOf(id));
        exportList.setStatus("5");

        exportListService.updateStatusById(exportList);

        return "redirect:/displayexportlists";
    }

    @GetMapping("/stayitemdetail")
    public String stayItemDetail(String id,Model model){
        TempSaveItems tempSaveItems = tempSaveService.selectAllByIdCustom(Integer.parseInt(id));
        if (!tempSaveItems.getStatus().equals("5")){
            return "redirect:/excoldstorageitem";
        }
        tempSaveItems.setDates(DateAndTimeStamp.timeStampToDate(tempSaveItems.getRegistrationTimeStamp()));
        AddColdStorageRoomInformation room = roomInformationService.getRoomByRoomCode(tempSaveItems.getRoomCode());
        tempSaveItems.setRoomLabelName(room.getRoomLabelName());
        tempSaveItems.setRoomName(room.getRoomName());
        model.addAttribute("tempSaveItems",tempSaveItems);
        //从库房表中获得货物入库时间
        ItemInformation itemInformation = itemService.selectByTempId(tempSaveItems.getRoomCode(), String.valueOf(tempSaveItems.getId()));
        itemInformation.setDate(DateAndTimeStamp.timeStampToDate(itemInformation.getRegistrationTimeStamp()));
        model.addAttribute("itemInformation",itemInformation);

        model.addAttribute("room",room);
        return "stayItemDetail";
    }

}
