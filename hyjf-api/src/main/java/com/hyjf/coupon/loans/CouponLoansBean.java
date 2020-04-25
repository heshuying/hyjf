package com.hyjf.coupon.loans;

import com.hyjf.base.bean.BaseBean;


public class CouponLoansBean extends BaseBean {
	
	/**
	 * 标的编号
	 */
	String borrowNid;
	
	/**
	 * 用户编号
	 */
	Integer userId;
	/**
	 * 出借编号
	 */
	String nid;
	
	/**
	 * 订单号
	 */
	String orderId;
	
	/**
	 * 优惠券出借订单号
	 */
	String orderIdCoupon;

	public String getBorrowNid() {
		return borrowNid;
	}

	public void setBorrowNid(String borrowNid) {
		this.borrowNid = borrowNid;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	
	public String getNid() {
		return nid;
	}

	public void setNid(String nid) {
		this.nid = nid;
	}

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

	public String getOrderIdCoupon() {
		return orderIdCoupon;
	}

	public void setOrderIdCoupon(String orderIdCoupon) {
		this.orderIdCoupon = orderIdCoupon;
	}
	
	

}
