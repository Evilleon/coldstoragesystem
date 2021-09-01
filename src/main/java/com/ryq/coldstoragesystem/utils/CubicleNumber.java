package com.ryq.coldstoragesystem.utils;

import org.apache.tomcat.util.buf.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CubicleNumber {

    public static List<String> generateCode(int length,int width){

        char[] alphabet = {'A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z'};
        //按照Excel的编号顺序，长为字母，宽为数字，从1开始，即A1代表，左上1号仓位
        //  A  B  C  D  E  F
        //1 A1 B1 C1 D1 E1 F1
        //2 A2 B2 C2 D2 E2 F2
        //3 A3 B3 C3 D3 E3 F3
        if (length<width) {
            int flag = width;
            width = length;
            length = flag;
        }
        List<String> code = new ArrayList<>();
        if (length <= 26){
            int len=0;
            for(;len<length;len++){
                int wid=1;
                for (;wid<=width;wid++){
                    code.add(String.valueOf(alphabet[len])+wid);
                }
            }
        }
        else{
            int len=0;
            for(;len<26;len++){
                int wid=1;
                for (;wid<=width;wid++){
                    code.add(String.valueOf(alphabet[len])+wid);
                }
            }
            //横向编完了26个字母还有未编号的，最后一个是Z，接下来就是AA，AB...ZZ
            int unCount = length-26;
            int len1 = 0;
            for (;len1<26;len1++){
                int len2=0;
                for (;len2<26;len2++){
                    int count = 1;
                    for (;count<=width;count++){
                        code.add(String.valueOf(alphabet[len1])+String.valueOf(alphabet[len2])+count);
                    }
                    unCount--;
                    System.out.println("未编行数："+unCount);
                    if (unCount <= 0){
                        break;
                    }
                }
                if (unCount <= 0){
                    break;
                }
            }
        }

        return code;
    }

    public static void main(String[] args) {
//        List<String> codes = generateCode(28, 28);
//        for (String c:codes){
//            System.out.println(c+" ");
//        }
        String nums = "a,b,c";
        String[] split = nums.split(",");
        String join = StringUtils.join(Arrays.asList(split), ',');
        System.out.println(join);
    }
}
