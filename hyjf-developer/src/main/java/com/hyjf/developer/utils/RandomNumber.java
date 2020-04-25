package com.hyjf.developer.utils;

import java.util.Random;

public class RandomNumber {
    
    /**
     * 
     * 生成测试用的手机号
     * @author hsy
     * @return
     */
    public static String getPhoneNum() {
          int[] pre = { 160, 161, 162, 163, 164, 165};
//        int[] pre = { 134, 135, 136, 137, 138, 139, 150, 151, 152, 188, 130, 131, 132, 155, 156, 186,133, 153, 189 };
//        int[] Lian = { 130, 131, 132, 155, 156, 186 };
//        int[] Dian = { 133, 153, 189 };

        int rand = (int) (Math.random() * 100000000);

        String phoneNum = "";
        
        int yi = (int) (Math.random() * pre.length);
        int yy1 = pre[yi];
        if (rand < 10000000) {
            getPhoneNum();
        } else {
            phoneNum = String.valueOf(yy1) + rand;
        }
        
        return phoneNum;
    }
    
    /**
     * 
     * 生成指定长度的随机密码
     * @author hsy
     * @param length
     * @return
     */
    public static String getRandomPassword(int length) {
        Random random = new Random();
        String result = "";
        for (int i = 0; i < length; i++) {
            result += random.nextInt(10);
        }
        return result;
    }

    public static void main(String[] args) {
        System.out.println(getRandomPassword(6));
    }
}
