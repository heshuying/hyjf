package com.hyjf.invest;

import java.io.Serializable;

import org.apache.commons.lang3.StringUtils;

import com.hyjf.base.bean.BaseBean;

public class InvestBean extends BaseBean implements Serializable  {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2569482809922162226L;
	//出借标号
	private String borrowNid;
	//平台标识
    private String platform="1";
	//真实出借金额
    private String money="0";
    //优惠券编号
    private String couponGrantId;
    //出借设备ip
    private String ip;
    //真实出借订单号
    private String ordId=StringUtils.EMPTY;
    //排他check
    private int couponOldTime;
    

    public String getBorrowNid() {
        return borrowNid;
    }

    public void setBorrowNid(String borrowNid) {
        this.borrowNid = borrowNid;
    }
    
    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getCouponGrantId() {
        return couponGrantId;
    }

    public void setCouponGrantId(String couponGrantId) {
        this.couponGrantId = couponGrantId;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getCouponOldTime() {
        return couponOldTime;
    }

    public void setCouponOldTime(int couponOldTime) {
        this.couponOldTime = couponOldTime;
    }

    public String getOrdId() {
        return ordId;
    }

    public void setOrdId(String ordId) {
        this.ordId = ordId;
    }
    
    
}
