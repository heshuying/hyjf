package com.hyjf.coupon.repay;

import com.hyjf.base.bean.BaseBean;

public class CouponRepayBean extends BaseBean {
	/**
     * 当前期数
     */
	private Integer periodNow;
	/**
	 * 体验金按收益期限还款的出借编号列表
	 */
	private String nidList;
	/**
     *  借款编号（直投类）
     */
	private String borrowNid;
	/**
     *  计划编号（汇添金）
     */
	private String planNid;
	
	/**
	 * 出借/加入订单号
	 */
	private String orderId;
	
    public Integer getPeriodNow() {
        return periodNow;
    }
    public void setPeriodNow(Integer periodNow) {
        this.periodNow = periodNow;
    }
    public String getBorrowNid() {
        return borrowNid;
    }
    public void setBorrowNid(String borrowNid) {
        this.borrowNid = borrowNid;
    }
	public String getNidList() {
		return nidList;
	}
	public void setNidList(String nidList) {
		this.nidList = nidList;
	}
	public String getPlanNid() {
		return planNid;
	}
	public void setPlanNid(String planNid) {
		this.planNid = planNid;
	}
    public String getOrderId() {
        return orderId;
    }
    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }
	
}
