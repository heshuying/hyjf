package com.hyjf.activity.newyear.wealthcard;

import com.hyjf.base.bean.BaseBean;

public class WealthCardBean extends BaseBean{

    // 手机号码
    private String phoneNum = "";
    
    // 财神卡编号：1：金，2：鸡，3：纳，4：福
    private int cardIdentifier;

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public int getCardIdentifier() {
        return cardIdentifier;
    }

    public void setCardIdentifier(int cardIdentifier) {
        this.cardIdentifier = cardIdentifier;
    }

    
    
}
