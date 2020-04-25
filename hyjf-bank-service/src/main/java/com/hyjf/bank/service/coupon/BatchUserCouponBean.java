package com.hyjf.bank.service.coupon;

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
	
	private String timestamp;

	/**
	 * token sign
	 */
	private String sign;
	
	
	private String usercoupons;


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
	public String getUsercoupons() {
		return usercoupons;
	}


	/**
	 * @param usercoupons the usercoupons to set
	 */
	public void setUsercoupons(String usercoupons) {
		this.usercoupons = usercoupons;
	}


	/**
	 * @return the timestamp
	 */
	public String getTimestamp() {
		return timestamp;
	}


	/**
	 * @param timestamp the timestamp to set
	 */
	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}
	
	
    
}
