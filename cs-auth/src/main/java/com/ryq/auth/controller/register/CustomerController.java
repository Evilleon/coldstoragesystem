package com.ryq.auth.controller.register;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ryq.coldstoragesystem.bean.Customer;
import com.ryq.coldstoragesystem.bean.User;
import com.ryq.coldstoragesystem.service.CustomerService;
import com.ryq.coldstoragesystem.utils.VerifyData;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;
import javax.xml.ws.http.HTTPException;
import java.io.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

@Controller
public class CustomerController {

    @Autowired
    CustomerService customerService;

    //跳转新增客户页面
    @GetMapping("/toinsertcustomer")
    public String toInsertCustomer(Model model,Customer customer){
        model.addAttribute("currentdata",customer);
        return "AddCustomer";
    }
    //入库
    @GetMapping("/insertcustomer")
    public String insertCustomer(Model model, @Valid Customer customer, BindingResult bindingResult){
        HashMap<String, String> errorMap = new HashMap<>();
        if(bindingResult.hasErrors()){
            errorMap = VerifyData.validData(errorMap,bindingResult);
            model.addAttribute("errorMap",errorMap);
            model.addAttribute("currentdata",customer);
            return "AddCustomer";
        }
        User user = (User) SecurityUtils.getSubject().getPrincipal();
        customer.setCreater(user.getUserName());
        boolean b = customerService.insertCustomer(customer);

        return "index";
    }
    //客户展示
    @GetMapping("/displaycustomer")
    public String displayCustomers(Model model){
        User user = (User) SecurityUtils.getSubject().getPrincipal();
        List<Customer> customers = customerService.selectAllCustomerCustom(user.getUserName());
        model.addAttribute("customers",customers);
        return "DisplayCustomers";
    }
    //客户修改
    @GetMapping("/revisecustomer")
    public String reviseCustomer(Model model,String id){
        Customer customer = customerService.selectCustomerByIdCustom(Integer.parseInt(id));
        model.addAttribute("customers",customer);
        return "ReviseCustomer";
    }
    //修改入库
    @GetMapping("/reviseincustomer")
    public String reviseInCustomer(Model model, @Valid Customer customer, BindingResult bindingResult){
        HashMap<String, String> errorMap = new HashMap<>();
        if(bindingResult.hasErrors()){
            errorMap = VerifyData.validData(errorMap,bindingResult);
            model.addAttribute("errorMap",errorMap);
            model.addAttribute("currentdata",customer);
            return "ReviseCustomer";
        }
        User user = (User) SecurityUtils.getSubject().getPrincipal();
        customer.setCreater(user.getUserName());
        boolean b = customerService.updateCustomerById(customer);

        return "redirect:/displaycustomer";
    }

    @PostMapping("/getcustomerbyuid")
    @ResponseBody
    public JSONObject getCustomerByuId(String id){

//        JSONArray jsonArray = new JSONArray();
        Customer customer = customerService.selectCustomerByIdCustom(Integer.parseInt(id));

        String address=customer.getAddress();
        String ak="VovYGooPZpSU7yqTG1GeukhP1UVibGK2";
        String urlGetLngLat= "http://api.map.baidu.com/geocoding/v3/?address="+address+"&output=json&ak="+ak;

        /*   获得经纬度   */


        String resultGetLngLat = callBaiduMapAPI(urlGetLngLat);

        JSONObject jsStr = JSONObject.parseObject(resultGetLngLat);//toString()将StringBuilder转成String
        BigDecimal bigLng = (BigDecimal) jsStr.getJSONObject("result").getJSONObject("location").get("lng");
        BigDecimal bigLat = (BigDecimal) jsStr.getJSONObject("result").getJSONObject("location").get("lat");
        String lng = bigLng.setScale(6, RoundingMode.DOWN).toString();
        String lat = bigLat.setScale(6, RoundingMode.DOWN).toString();
        customer.setLng(lng);
        customer.setLat(lat);
        System.out.println(lng+","+lat);


        /*根据经纬度计算距离*/
        String startLng="114.509052";
        String startLat="38.082581";
        String endLng=customer.getLng();
        String endLat=customer.getLat();

        String start=startLat+","+startLng;

        String end=endLat+","+endLng;

//        System.out.println(start);
//        System.out.println(end);

        String urlGetDistance = "http://api.map.baidu.com/direction/v2/driving?origin="+start+"&destination="+end+"&ak=SVhaeGbNd2XwFkzVHWhhbTzCZi78Sqow&alternatives=0&tactics=2";


        String resultDistance = callBaiduMapAPI(urlGetDistance);
        //得到源数据，解析成JSONObject
        JSONObject jsObj = JSONObject.parseObject(resultDistance);
        //得到key值为result的value
        JSONObject jsGetResult = (JSONObject) jsObj.get("result");
        //得到routes的value（因为是数组，需要转一下）
        JSONArray jsArrGetRoutes = (JSONArray) jsGetResult.get("routes");
        //转成正常的JSONObject
        JSONObject jsGetRoutes = (JSONObject) jsArrGetRoutes.get(0);
        Integer distance = (Integer) jsGetRoutes.get("distance");
        BigDecimal dis = new BigDecimal((float)distance/1000);
        String distanc = dis.setScale(3, RoundingMode.DOWN).toString();
        customer.setDistance(distanc);
//        JSONObject jsGetRoutes = (JSONObject)jsGetResult.get("routes")[0];
//        System.out.println("jsGetRoutes:"+jsGetRoutes.get("distance"));
        //


        /*   我是分界线   */

        System.out.println(customer);

        JSONObject jsonObject = (JSONObject) JSONObject.toJSON(customer);
//        jsonArray.add(jsonObject);
        System.out.println(jsonObject);

        return jsonObject;
    }

    @GetMapping("/deletecustomer")
    public String deleteCustomerById(String id){

        customerService.deleteCustomById(Integer.parseInt(id));

        return "redirect:/displaycustomer";
    }

    private String callBaiduMapAPI(String myUrl) {
//        PrintWriter out = null;
        BufferedReader in = null;
        StringBuilder result = new StringBuilder();
        try{
            URL url=new URL(myUrl);
            URLConnection urlConnection = url.openConnection();
            urlConnection.setDoOutput(true);
            urlConnection.setDoInput(true);
//            out = new PrintWriter(new OutputStreamWriter(urlConnection.getOutputStream(), StandardCharsets.UTF_8));
//            out.flush();
            in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(), StandardCharsets.UTF_8));
            String line;
            while ((line=in.readLine())!=null){
                result.append(line);
            }
            //将lng和lat取出
//            System.out.println(result);
        }catch (Exception e){
            System.out.println(e);
        }



        return result.toString();
    }
}
