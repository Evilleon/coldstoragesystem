package room.entity;

import cn.hutool.core.util.RandomUtil;
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

    /**
     * 构建序列号 规则：日期（8）+操作员编号（3）+仓库编号（3）+ 随机编号（6）
     * @param roomCode
     * @return
     */
    public static String reGenerateIdentifier(String roomCode){
        // 获取当前时间
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
        // 转换为String类型
        String date = simpleDateFormat.format(new Date());
        // 操作员编号
        User user = (User) SecurityUtils.getSubject().getPrincipal();
        // 随机编号
        String randomNumbers = RandomUtil.randomNumbers(6);
        // 组装并返回
        return date + user + roomCode + randomNumbers;
    }
}
