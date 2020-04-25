/*
 * Copyright(c) 2012-2016 JD Pharma.Ltd. All Rights Reserved.
 */
package com.hyjf.wechat.controller.hjh.vo;

import com.hyjf.wechat.base.BaseBean;

public class WxHJHTenderVo extends BaseBean {
    
    
    private String borrowNid;

    private String account;

    private String interest;
    
    private String couponGrantId;

    private String presetProps;

    public String getBorrowNid() {
        return borrowNid;
    }

    public void setBorrowNid(String borrowNid) {
        this.borrowNid = borrowNid;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getInterest() {
        return interest;
    }

    public void setInterest(String interest) {
        this.interest = interest;
    }

	public String getCouponGrantId() {
		return couponGrantId;
	}

	public void setCouponGrantId(String couponGrantId) {
		this.couponGrantId = couponGrantId;
	}

    public String getPresetProps() {
        return presetProps;
    }

    public void setPresetProps(String presetProps) {
        this.presetProps = presetProps;
    }
}
