package com.hyjf.server.util;

import java.util.Arrays;
import java.util.Random;
import java.util.UUID;

import org.apache.commons.lang3.RandomUtils;

import com.hyjf.common.util.GetOrderIdUtils;
import com.hyjf.common.validator.Validator;

/**
 * 
 * 安全工具类
 * 
 * @author renxingchen modify by 朱晓东 at 2016.8.22
 * @version hyjf 1.0
 * @since hyjf 1.0 2016年2月18日
 * @see 上午9:21:27
 */
public class SecretUtil {

    private static String letter = "abcdefghigklmnopqrstuvwxyzABCDEFGHIGKLMNOPQRSTUVWXYZ";
    /**
     * 生成唯一标识符
     *
     * 生成规则： uuid + 三位随机字母 + 三位随机数
     *
     * @return
     */
    public static String createSign() {
        String sign = UUID.randomUUID().toString() + RandomUtils.nextInt(0, 9);
        Random random = new Random();
        // 生成随机三位字母
        for (int i = 0; i < 3; i++) {
            sign = sign + letter.charAt(random.nextInt(52));
        }
        // 生成一个随机的三位数
        sign = sign + GetOrderIdUtils.getRandomNum(1000);

        return sign;
    }
 
    /**
     * 字符串转换为ascii
     *
     * @param content
     * @return
     */
    public static String stringToAscii(String content) {
        String result = "";
        int max = content.length();
        for (int i = 0; i < max; i++) {
            char c = content.charAt(i);
            int b = (int) c;
            result = result + b;
        }
        return result;
    }

    /**
     * 字符串按照ascii排序
     *
     * @param content
     * @return
     */
    public static String sortByAscii(String content) {

        if (Validator.isNull(content)) {
            return "";
        }
        char[] ch = content.toCharArray();
        Arrays.sort(ch);

        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < ch.length; i++) {
            sb.append(ch[i]);
        }

        return sb.toString();
    }
}
