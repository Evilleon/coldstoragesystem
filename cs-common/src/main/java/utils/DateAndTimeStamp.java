package utils;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateAndTimeStamp {

    public static String DateToTimeStamp(String date) throws ParseException {
        if (date == null) {
            //获取当前时间
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");//设置日期格式
            date = df.format(new Date());
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date time = simpleDateFormat.parse(date);
        long timeStamp = time.getTime();
        return Long.toString(timeStamp);
    }

    public static String timeStampToDate(String RegistrationTimeStamp) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(new Date(Long.parseLong(RegistrationTimeStamp)));
    }

    public static String timeStampToDateYMD(String RegistrationTimeStamp) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        return sdf.format(new Date(Long.parseLong(RegistrationTimeStamp)));
    }
}
