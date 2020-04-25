package com.hyjf.api.aems.bankcard;

import com.hyjf.base.bean.BaseBean;

public class AemsBankCardRequestBean extends BaseBean{

    /** 姓名 */
    private String userName;

    /** 身份证号 */
    private String cardNo;

    /** 手机号 */
    private String mobile;

    /** 银行卡号 */
    private String code;

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
    
    
}
