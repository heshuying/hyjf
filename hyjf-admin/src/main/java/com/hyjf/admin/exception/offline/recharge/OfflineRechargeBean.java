package com.hyjf.admin.exception.offline.recharge;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 取得用户线下充值列表 表单类
 * zhangjinpeng
 */
public class OfflineRechargeBean implements Serializable {

	/**
     * serialVersionUID
     */
	private static final long serialVersionUID = 1790601499725213969L;

    private String ids;
    public String getIds() {
        return ids;
    }

    public void setIds(String ids) {
        this.ids = ids;
    }
    /******************查询条件START******************/
    /**
     * 用户名
     */
    private String usernameSrch;
    /**
     * 手机号码
     */
    private String phoneSrch;
    /**
     * 银行开户的电子账号
     */
    private String bankOpenAccountSrch;
    /**
     * 开始时间
     */
    private String startTimeSrch;
    /**
     * 结束时间
     */
    private String endTimeSrch;
    
    private String userId;
    
    private String username;
    
    private String bankOpenAccount;
    
    
    public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
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

	public String getUsernameSrch() {
		return usernameSrch;
	}

	public void setUsernameSrch(String usernameSrch) {
		this.usernameSrch = usernameSrch;
	}

	public String getPhoneSrch() {
		return phoneSrch;
	}

	public void setPhoneSrch(String phoneSrch) {
		this.phoneSrch = phoneSrch;
	}

	public String getBankOpenAccountSrch() {
		return bankOpenAccountSrch;
	}

	public void setBankOpenAccountSrch(String bankOpenAccountSrch) {
		this.bankOpenAccountSrch = bankOpenAccountSrch;
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
	/******************查询条件END******************/
	// 线下充值列表
	private List<ResultBean> recordList = new ArrayList<ResultBean>();
	public List<ResultBean> getRecordList() {
		return recordList;
	}

	public void setRecordList(List<ResultBean> recordList) {
		this.recordList = recordList;
	}
}
