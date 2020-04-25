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
public class WealthCardGiveResultBean extends BaseResultBean{

    /**
     * 此处为属性说明
     */
    private static final long serialVersionUID = 1L;
    
    /**
     * 0：成功  1：请求参数错误  2：手机号码对应的用户不存在  3：赠送的人不能是自己  4： 卡片数量不足  5: 卡片发送处理错误
     */
    private String errCode = "";

    private String cardName = "";
    
    private String phoneNum = "";
    
    private String userName = "";

    public String getCardName() {
        return cardName;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public String getUserName() {
        return userName;
    }

    public void setCardName(String cardName) {
        this.cardName = cardName;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getErrCode() {
        return errCode;
    }

    public void setErrCode(String errCode) {
        this.errCode = errCode;
    }
    
}
