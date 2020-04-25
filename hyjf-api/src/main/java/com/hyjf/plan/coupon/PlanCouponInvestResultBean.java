package com.hyjf.plan.coupon;

import java.io.Serializable;

import com.hyjf.base.bean.BaseResultBean;

public class PlanCouponInvestResultBean extends BaseResultBean implements Serializable  {


	private static final long serialVersionUID = 2569482809922162226L;
	// 优惠券收益
	private String couponInterest;
	// 优惠券类别
	private String couponType;
	// 优惠券类别
    private String couponTypeInt;
	// 优惠券额度
	private String couponQuota;
	//出借额度
	private String accountDecimal;
    public String getCouponInterest() {
        return couponInterest;
    }
    public void setCouponInterest(String couponInterest) {
        this.couponInterest = couponInterest;
    }
    public String getCouponType() {
        return couponType;
    }
    public void setCouponType(String couponType) {
        this.couponType = couponType;
    }
    public String getCouponQuota() {
        return couponQuota;
    }
    public void setCouponQuota(String couponQuota) {
        this.couponQuota = couponQuota;
    }
    public String getAccountDecimal() {
        return accountDecimal;
    }
    public void setAccountDecimal(String accountDecimal) {
        this.accountDecimal = accountDecimal;
    }
    public String getCouponTypeInt() {
        return couponTypeInt;
    }
    public void setCouponTypeInt(String couponTypeInt) {
        this.couponTypeInt = couponTypeInt;
    }
	
	
	
}
