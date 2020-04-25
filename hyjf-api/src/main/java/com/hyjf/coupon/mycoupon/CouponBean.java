package com.hyjf.coupon.mycoupon;

import com.hyjf.base.bean.BaseBean;

public class CouponBean extends BaseBean {
	/**
	 * 优惠券id
	 */
	private String id;
	
	/**
	 * 优惠券状态  0未使用，1已使用，4已过期
	 */
	private String couponStatus;
	
	/**
	 * 优惠券详情服务
	 */
	private String host;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCouponStatus() {
		return couponStatus;
	}

	public void setCouponStatus(String couponStatus) {
		this.couponStatus = couponStatus;
	}

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }
	
}
