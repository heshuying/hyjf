package com.hyjf.api.aems.synbalance;

import com.hyjf.base.bean.BaseBean;

public class AemsSynBalanceRequestBean extends BaseBean {
	/**
	 * 优惠券详情服务
	 */
	private String host;
	
	private String bankAccount;


    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

	public String getBankAccount() {
		return bankAccount;
	}

	public void setBankAccount(String bankAccount) {
		this.bankAccount = bankAccount;
	}
    
    
	
}
