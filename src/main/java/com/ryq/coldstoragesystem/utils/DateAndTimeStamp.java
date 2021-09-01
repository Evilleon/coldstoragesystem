package com.ryq.coldstoragesystem.utils;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateAndTimeStamp {

    public static String dateToTimeStamp() throws ParseException {
        //获取当前时间
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        String date = df.format(new Date());
        //生成时间戳
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date time = simpleDateFormat.parse(date);
        long timeStamp = time.getTime();
        return Long.toString(timeStamp);
    }

    public static String preDateToTimeStamp(String date) throws ParseException {
        //生成时间戳
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date time = simpleDateFormat.parse(date);
        long timeStamp = time.getTime();
        return Long.toString(timeStamp);
    }

    public static String timeStampToDate(String RegistrationTimeStamp){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(new Date(Long.parseLong(RegistrationTimeStamp)));
    }
    public static String timeStampToDateYMD(String RegistrationTimeStamp){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        return sdf.format(new Date(Long.parseLong(RegistrationTimeStamp)));
    }
}
