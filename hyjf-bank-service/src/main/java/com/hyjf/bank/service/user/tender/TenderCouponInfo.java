package com.hyjf.bank.service.user.tender;

import java.math.BigDecimal;

public class TenderCouponInfo {

	// 优惠券收益格式化
	private String couponInterest;
	// 优惠券收益
	private BigDecimal couponEarnings;
	// 优惠券类别
	private String couponType;
	// 优惠券类别
    private String couponTypeInt;
	// 优惠券额度
	private String couponQuota;
	//出借额度
	private String accountDecimal;
	//优惠券信息是否可用
	private String isSuccess;
	
	private String status;
	
	
	
	public BigDecimal getCouponEarnings() {
		return couponEarnings;
	}
	public void setCouponEarnings(BigDecimal couponEarnings) {
		this.couponEarnings = couponEarnings;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getIsSuccess() {
		return isSuccess;
	}
	public void setIsSuccess(String isSuccess) {
		this.isSuccess = isSuccess;
	}
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
	public String getCouponTypeInt() {
		return couponTypeInt;
	}
	public void setCouponTypeInt(String couponTypeInt) {
		this.couponTypeInt = couponTypeInt;
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
	
	
}
