package com.hyjf.common.util;

import com.hyjf.common.validator.Validator;

/**
 * 
 * 银行卡脱敏显示
 * @author sunss
 * @version hyjf 1.0
 * @since hyjf 1.0 2018年3月27日
 * @see 上午9:32:55
 */
public class BankCardUtil {

    /**
     * 
     * 银行卡脱敏显示
     * @author sunss
     * @param cardNo
     * @return
     */
    public static String getCardNo(String cardNo){
        String str = "";
        if(Validator.isNotNull(cardNo)&&cardNo.length()>10){
            str = "****  ****  **** " + cardNo.substring(cardNo.length() - 4, cardNo.length());
        }
        return str;
    }

    /**
     * 获取格式化后的银行卡，充值页面使用
     * add by cwyang
     * @param cardNo
     * @return
     */
    public static String getFormatCardNo(String cardNo){
        String str = "";
        if(Validator.isNotNull(cardNo)&&cardNo.length()>10){
            str = "尾号 " + cardNo.substring(cardNo.length() - 4, cardNo.length());
        }
        return str;
    }
}
