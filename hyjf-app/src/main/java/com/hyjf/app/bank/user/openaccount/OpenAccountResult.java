package com.hyjf.app.bank.user.openaccount;

import com.hyjf.app.BaseResultBeanFrontEnd;

import java.io.Serializable;

/**
 * Created by yaoyong on 2017/12/8.
 */
public class OpenAccountResult extends BaseResultBeanFrontEnd implements Serializable {

    /**
     * 序列化id
     */
    private static final long serialVersionUID = 3059306057064822540L;

    /**开户订单号*/
    private String logOrderId;

    /**跳转江西银行设置密码的URL*/
    private String passwordUrl;

    // 返回信息
    private String returnMsg;

    public String getReturnMsg() {
        return returnMsg;
    }

    public void setReturnMsg(String returnMsg) {
        this.returnMsg = returnMsg;
    }

    public String getLogOrderId() {
        return logOrderId;
    }

    public void setLogOrderId(String logOrderId) {
        this.logOrderId = logOrderId;
    }

    public String getPasswordUrl() {
        return passwordUrl;
    }

    public void setPasswordUrl(String passwordUrl) {
        this.passwordUrl = passwordUrl;
    }
}
