package com.hyjf.user.synbalance;

import com.hyjf.base.bean.BaseBean;

public class SynBalanceRequestBean extends BaseBean {
	/**
	 * 优惠券详情服务
	 */
	private String host;
	
	private String bankAccount;
	/**
     * 用户名
     */
    private String username;
    /**
     * 银行开户的电子账号
     */
    private String bankOpenAccount;
    /**
     * 开始时间
     */
    private String startTimeSrch;
    /**
     * 结束时间
     */
    private String endTimeSrch;

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

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getBankOpenAccount() {
		return bankOpenAccount;
	}

	public void setBankOpenAccount(String bankOpenAccount) {
		this.bankOpenAccount = bankOpenAccount;
	}

	public String getStartTimeSrch() {
		return startTimeSrch;
	}

	public void setStartTimeSrch(String startTimeSrch) {
		this.startTimeSrch = startTimeSrch;
	}

	public String getEndTimeSrch() {
		return endTimeSrch;
	}

	public void setEndTimeSrch(String endTimeSrch) {
		this.endTimeSrch = endTimeSrch;
	}
    
    
	
}
