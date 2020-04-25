package com.hyjf.wechat.controller.user.bankopenbg;

import com.hyjf.wechat.base.BaseResultBean;

/**
 * Created by jijun on 2018/02/26
 */
public class OpenAccountResult extends BaseResultBean {

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

    /**手机号*/
    private String mobile;

    /**用户名*/
    private String userName;

    
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

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
