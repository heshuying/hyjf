package com.hyjf.app.bank.user.bindCard;

import com.hyjf.app.BaseBean;
import com.hyjf.app.BaseResultBeanFrontEnd;

import java.io.Serializable;

/**
 * @author fuqiang
 */
public class BindCardBean extends BaseResultBeanFrontEnd {

    private static final long serialVersionUID = 4562252189828095088L;

    /** 真实姓名 */
    private String userName;

    /** 身份证号码 */
    private String userCardId;

    /** 银行卡号码 */
    private String bankId;

    /** 手机号码 */
    private String telNo;

    /** 验证码 */
    private String code;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserCardId() {
        return userCardId;
    }

    public void setUserCardId(String userCardId) {
        this.userCardId = userCardId;
    }

    public String getBankId() {
        return bankId;
    }

    public void setBankId(String bankId) {
        this.bankId = bankId;
    }

    public String getTelNo() {
        return telNo;
    }

    public void setTelNo(String telNo) {
        this.telNo = telNo;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
