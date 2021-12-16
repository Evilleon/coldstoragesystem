package com.ryq.common.utils;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.lang.reflect.Field;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Author Ren YuQi
 * @Create 2021-10-28 14:08
 */
public class ValidateUtils {

    private final static Log logger = LogFactory.getLog(ValidateUtils.class);

    public static String validate(Object obj){
        StringBuilder stringBuilder = new StringBuilder();
        // 获取所有字段 不包括父类中的字段
        Field[] fields = obj.getClass().getDeclaredFields();
        // 分别处理
        for(Field field:fields){
            logger.info(field+" >>>>> ");
            // 返回field上的valid注释内容
            Valid valid = field.getAnnotation(Valid.class);
            // 关闭安全检查，提高反射速度
            field.setAccessible(true);
            try {
                String value = "";
                // 返回指定对象obj上此 field 表示的字段的值
                if (field.get(obj) != null){
                    value = String.valueOf(field.get(obj));
                }
                if (valid != null && !"".equals(valid.pattern())){
                    // 使用正则匹配 规则是valid属性pattern的值 被检验的值是刚获得的value
                    Matcher matcher = Pattern.compile(valid.pattern()).matcher(value);
                    if(!matcher.matches()){
                        stringBuilder.append(valid.tipMsg()).append(";");
                    }
                }

            } catch (IllegalAccessException e) {
                logger.info("字段校验失败，字段名称：" + field.getName(), e);
            }
        }

        return stringBuilder.toString();
    }
}
