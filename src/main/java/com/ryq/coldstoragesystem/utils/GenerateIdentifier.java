package com.ryq.coldstoragesystem.utils;

import com.ryq.coldstoragesystem.bean.User;
import org.apache.shiro.SecurityUtils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class GenerateIdentifier {

    //编号命名规则：操作员编号+入库物品编号+库房编号（数字部分）+当前时间（秒）
    public static String generateIdentifier(String roomLabelCode,String roomCode){

        //获取当前时间
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");//设置日期格式
        String date = df.format(new Date());
        //操作员编号
        User user = (User) SecurityUtils.getSubject().getPrincipal();

        StringBuilder digitCode= new StringBuilder();
        for (int i=0;i<roomCode.length();i++){
            if (Character.isDigit(roomCode.charAt(i))){
                digitCode.append(roomCode.charAt(i));
            }
        }
        return user.getIdentifier()+roomLabelCode+digitCode+date;
    }
    public static String generateIdentifier(String roomLabelCode){

        //获取当前时间
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");//设置日期格式
        String date = df.format(new Date());
        //操作员编号
        User user = (User) SecurityUtils.getSubject().getPrincipal();

        return user.getIdentifier()+roomLabelCode+ "" +date;
    }
}
