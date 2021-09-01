package com.ryq.coldstoragesystem.controller;

import com.ryq.coldstoragesystem.bean.Transport;
import com.ryq.coldstoragesystem.bean.User;
import com.ryq.coldstoragesystem.service.TransportService;
import com.ryq.coldstoragesystem.utils.VerifyData;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;

@Controller
public class TransportController {

    @Autowired
    TransportService transportService;

    //跳转新增运输工具页面
    @GetMapping("/toinserttransport")
    public String toInsertTransport(Model model, Transport transport){

        model.addAttribute("currentdata",transport);

        return "AddTransport";

    }

    //运输工具入库
    @GetMapping("/inserttransport")
    public String InsertTansport(Model model, @Valid Transport transport, BindingResult bindingResult){

        HashMap<String, String> errorMap = new HashMap<>();
        if(bindingResult.hasErrors()){
            errorMap = VerifyData.validData(errorMap,bindingResult);
            model.addAttribute("errorMap",errorMap);
            model.addAttribute("currentdata",transport);
            return "AddTransport";
        }

        boolean isInsert = transportService.insertTransport(transport);

        return "redirect:/displaytransport";
    }
    //跳转查看运输工具页面
    @GetMapping("/todisplaytransport")
    public String displayTransport(Model model){

        List<Transport> transports = transportService.selectAllTransport();
        model.addAttribute("transports",transports);

        return "DisplayTransport";
    }


    @GetMapping("/revisetransport")
    public String reviseTransport(Model model,String id){

        Transport transport = transportService.selectTransportById(Integer.parseInt(id));
        model.addAttribute("transport",transport);
        return "ReviseTransport";
    }
    
    @GetMapping("/reviseintransport")
    public String reviseInTransport(Model model, @Valid Transport transport, BindingResult bindingResult){
        HashMap<String, String> errorMap = new HashMap<>();
        if(bindingResult.hasErrors()){
            errorMap = VerifyData.validData(errorMap,bindingResult);
            model.addAttribute("errorMap",errorMap);
            model.addAttribute("currentdata",transport);
            return "ReviseTransport";
        }
        transportService.updateTransportById(transport);

        return "redirect:/displaytransport";
    }

    @GetMapping("/deletetransport")
    public String deleteTransport(String id){

        transportService.deleteTransportById(Integer.parseInt(id));

        return "redirect:/displaytransport";
    }

}
