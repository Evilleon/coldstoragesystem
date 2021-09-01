package com.ryq.coldstoragesystem.utils;

import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

import java.util.HashMap;
import java.util.List;

/**
 * 验证数据合法性
 */
public class VerifyData {

    /**
     * 验证数据合法性
     */
    public static HashMap<String,String> validData(HashMap<String, String> errorMap, BindingResult bindingResult){
        List<ObjectError> labelErrorList = bindingResult.getAllErrors();
        for (ObjectError objectError : labelErrorList) {
            FieldError error = (FieldError) objectError;
            String field = error.getField();
            String message = error.getDefaultMessage();
            System.out.println(field + "出错" + "错误信息:" + message);
            errorMap.put(field,message);
        }
        return errorMap;
    }
}
