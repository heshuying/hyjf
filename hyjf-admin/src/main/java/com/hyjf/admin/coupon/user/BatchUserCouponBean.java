package com.hyjf.admin.coupon.user;

import java.io.Serializable;
import java.util.List;

public class BatchUserCouponBean implements Serializable  {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5378797562451703413L;

	/**
	 * 用户id
	 */
	private String userId;

	/**
	 * token sign
	 */
	private String sign;
	
	
	private List<BatchSubUserCouponBean> usercoupons;


	/**
	 * @return the userId
	 */
	public String getUserId() {
		return userId;
	}


	/**
	 * @param userId the userId to set
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}


	/**
	 * @return the sign
	 */
	public String getSign() {
		return sign;
	}


	/**
	 * @param sign the sign to set
	 */
	public void setSign(String sign) {
		this.sign = sign;
	}


	/**
	 * @return the usercoupons
	 */
	public List<BatchSubUserCouponBean> getUsercoupons() {
		return usercoupons;
	}


	/**
	 * @param usercoupons the usercoupons to set
	 */
	public void setUsercoupons(List<BatchSubUserCouponBean> usercoupons) {
		this.usercoupons = usercoupons;
	}
	
    
}
