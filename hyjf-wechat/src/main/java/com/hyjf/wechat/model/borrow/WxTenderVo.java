/*
 * Copyright(c) 2012-2016 JD Pharma.Ltd. All Rights Reserved.
 */
package com.hyjf.wechat.model.borrow;


/**
 * app客户端请求参数对象
 * 
 * @author 王坤
 * @version hyjf 1.0
 * @since hyjf 1.0 2016年2月19日
 * @see 下午1:55:44
 */
public class WxTenderVo  {
    //标的编号
	private String borrowNid;
	//出借金额
	private String account;

	//优惠券编号
	private String couponGrantId;
	
	private String sign;
	
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


    public String getCouponGrantId() {
        return couponGrantId;
    }

    public void setCouponGrantId(String couponGrantId) {
        this.couponGrantId = couponGrantId;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

}
