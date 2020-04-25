package com.hyjf.admin.manager.hjhplan.planrepay;

import com.hyjf.mybatis.model.auto.HjhRepay;
/**
 * 类增加ismonth和borrow_style字段
 * @author kdl
 *
 */
public class HjhRepayBean extends HjhRepay {
	/*
	 * 锁定期天月
	 */
	private Integer isMonth;
	/*
	 * 还款方式
	 */
	private String borrowStyle;
	
	// 汇计划三期新增
	/*
	 * 还款方式
	 */
	private String expectApr;
	/*
	 * 清算服务费（元）
	 */
	private String lqdServiceFee;
	/*
	 * 清算服务费率
	 */
	private String lqdServiceApr;
	/*
	 * 出借服务费率
	 */
	private String investServiceApr;
	/*
	 * 清算进度
	 */
	private String lqdProgress;
	/*
	 * 推荐人用户名
	 */
	private String inviteUser;
	
	public Integer getIsMonth() {
		return isMonth;
	}
	public void setIsMonth(Integer isMonth) {
		this.isMonth = isMonth;
	}
	public String getBorrowStyle() {
		return borrowStyle;
	}
	public void setBorrowStyle(String borrowStyle) {
		this.borrowStyle = borrowStyle;
	}
	public String getExpectApr() {
		return expectApr;
	}
	public void setExpectApr(String expectApr) {
		this.expectApr = expectApr;
	}
	public String getLqdServiceFee() {
		return lqdServiceFee;
	}
	public void setLqdServiceFee(String lqdServiceFee) {
		this.lqdServiceFee = lqdServiceFee;
	}
	public String getLqdServiceApr() {
		return lqdServiceApr;
	}
	public void setLqdServiceApr(String lqdServiceApr) {
		this.lqdServiceApr = lqdServiceApr;
	}
	public String getInvestServiceApr() {
		return investServiceApr;
	}
	public void setInvestServiceApr(String investServiceApr) {
		this.investServiceApr = investServiceApr;
	}
	public String getLqdProgress() {
		return lqdProgress;
	}
	public void setLqdProgress(String lqdProgress) {
		this.lqdProgress = lqdProgress;
	}
	public String getInviteUser() {
		return inviteUser;
	}
	public void setInviteUser(String inviteUser) {
		this.inviteUser = inviteUser;
	}
}
