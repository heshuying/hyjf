package com.hyjf.api.aems.mergeauth;

import com.hyjf.base.bean.BaseBean;

/**
 * 缴费授权请求Bean
 * 
 */
public class AemsMergeAuthPagePlusRequestBean extends BaseBean {

	private  String accountId;
	private  String forgotPwdUrl;
	private  String notifyUrl;
	
	private  String authType;

    @Override
    public String getAccountId() {
        return accountId;
    }

    @Override
    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getForgotPwdUrl() {
        return forgotPwdUrl;
    }

    public void setForgotPwdUrl(String forgotPwdUrl) {
        this.forgotPwdUrl = forgotPwdUrl;
    }

    public String getNotifyUrl() {
        return notifyUrl;
    }

    public void setNotifyUrl(String notifyUrl) {
        this.notifyUrl = notifyUrl;
    }

	public String getAuthType() {
		return authType;
	}

	public void setAuthType(String authType) {
		this.authType = authType;
	}
    
    
}
