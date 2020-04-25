package com.hyjf.activity.newyear.wealthcard;

import com.hyjf.base.bean.BaseResultBean;

/**
 * 
 * 财富卡
 * @author hsy
 * @version hyjf 1.0
 * @since hyjf 1.0 2017年1月9日
 * @see 下午3:37:37
 */
public class WealthCardPhoneCheckResultBean extends BaseResultBean{

    /**
     * 此处为属性说明
     */
    private static final long serialVersionUID = 1L;

    /**
     * 手机号
     */
    private String phoneNum;
    
    /**
     * 手机号对应用户名
     */
    private String userName;
    
    /**
     * 是否是有效的手机号码   0：有效   1：无效
     */
    private int isValidPhoneNum;

    public String getPhoneNum() {
        return phoneNum;
    }

    public String getUserName() {
        return userName;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getIsValidPhoneNum() {
        return isValidPhoneNum;
    }

    public void setIsValidPhoneNum(int isValidPhoneNum) {
        this.isValidPhoneNum = isValidPhoneNum;
    }
    
    
}
