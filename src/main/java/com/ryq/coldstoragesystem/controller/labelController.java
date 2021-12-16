package com.ryq.coldstoragesystem.controller;

import com.ryq.coldstoragesystem.bean.RoomLabel;
import com.ryq.coldstoragesystem.service.LabelService;
import com.ryq.coldstoragesystem.utils.VerifyData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;

@Controller
public class labelController {


    @Autowired
    LabelService labelService;


    /**
     * 新增标签
     */
    @GetMapping("/addcustomlabel")
    public String addCustomLabel(Model model, RoomLabel roomLabel){
        model.addAttribute("currentdata",roomLabel);
        return "AddCustomLabel";
    }

    /**
     * 保存标签
     */
    @GetMapping("/savelabel")
    public String saveLabel(Model model, @Valid RoomLabel roomLabel, BindingResult bindingResult){
        HashMap<String, String> errorMap = new HashMap<>();
        if(bindingResult.hasErrors()){
            errorMap = VerifyData.validData(errorMap,bindingResult);
            model.addAttribute("errorMap",errorMap);
            model.addAttribute("currentdata",roomLabel);
            return "AddCustomLabel";
        }else {
            List<RoomLabel> list = labelService.lambdaQuery().eq(RoomLabel::getRoomLabelName, roomLabel.getRoomLabelName())
                    .or().eq(RoomLabel::getRoomLabelCode, roomLabel.getRoomLabelCode()).list();
            if (!roomLabel.getRoomLabelName().isEmpty() && !roomLabel.getRoomLabelCode().isEmpty() && list.size() == 0) {
                boolean save = labelService.save(roomLabel);
                model.addAttribute("msg", save);
            } else {
                model.addAttribute("msg", "false,请检查数据是否重复或为空！");
            }
            return "redirect:/displaylabel";
        }
    }

    //展示冷库标签
    @GetMapping("/displaylabel")
    public String displayLabel(Model model){

        List<RoomLabel> labels = labelService.getLabels();

        model.addAttribute("labels",labels);

        return "DisplayLabel";

    }

    @GetMapping("/deletelabel")
    public String deleteLabel(String id){

        labelService.deleteById(Integer.valueOf(id));
        return "redirect:/displaylabel";
    }


}
