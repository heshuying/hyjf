
package com.hyjf.mybatis.model.customize.admin;

import java.io.Serializable;

/**
 * 充值监控
 * 
 * @author 孙亮
 * @since 2016年1月20日 下午4:28:29
 */
public class AdminRechargeWarnExceptionCustomize implements Serializable {

	/**
	 * serialVersionUID:
	 */

	private static final long serialVersionUID = 1L;
	private String userId;// 用户id
	private String userName;// 用户名
	private String addtime;// 日期
	private String totalwithdraw;// 总提现金额
	private String totalrecharge;// 总充值金额
	private String userNameSearch;// 用户名
	private String startDateSearch;// 查询添加时间开始日期
	private String endDateSearch;// 查询添加时间结束日期
	/**
	 * 检索条件 limitStart
	 */
	private int limitStart = -1;

	/**
	 * 检索条件 limitEnd
	 */
	private int limitEnd = -1;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getAddtime() {
		return addtime;
	}

	public void setAddtime(String addtime) {
		this.addtime = addtime;
	}

	public String getTotalwithdraw() {
		return totalwithdraw;
	}

	public void setTotalwithdraw(String totalwithdraw) {
		this.totalwithdraw = totalwithdraw;
	}

	public String getTotalrecharge() {
		return totalrecharge;
	}

	public void setTotalrecharge(String totalrecharge) {
		this.totalrecharge = totalrecharge;
	}

	public String getUserNameSearch() {
		return userNameSearch;
	}

	public void setUserNameSearch(String userNameSearch) {
		this.userNameSearch = userNameSearch;
	}

	public String getStartDateSearch() {
		return startDateSearch;
	}

	public void setStartDateSearch(String startDateSearch) {
		this.startDateSearch = startDateSearch;
	}

	public String getEndDateSearch() {
		return endDateSearch;
	}

	public void setEndDateSearch(String endDateSearch) {
		this.endDateSearch = endDateSearch;
	}

	public int getLimitStart() {
		return limitStart;
	}

	public void setLimitStart(int limitStart) {
		this.limitStart = limitStart;
	}

	public int getLimitEnd() {
		return limitEnd;
	}

	public void setLimitEnd(int limitEnd) {
		this.limitEnd = limitEnd;
	}

}
