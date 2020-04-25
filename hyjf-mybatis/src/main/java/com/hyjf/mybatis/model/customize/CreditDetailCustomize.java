/**
 * Description:（类功能描述-必填） 需要在每个方法前添加业务描述，方便业务业务行为的BI工作
 * Copyright: Copyright (HYJF Corporation)2015
 * Company: HYJF Corporation
 * @author: Administrator
 * @version: 1.0
 * Created at: 2015年11月20日 下午5:24:10
 * Modification History:
 * Modified by : 
 */

package com.hyjf.mybatis.model.customize;

import java.io.Serializable;

/**
 * @author Administrator
 */

public class CreditDetailCustomize implements Serializable {

	/**
	 * serialVersionUID:
	 */

	private static final long serialVersionUID = 1L;
	/**
	 * 排序
	 */
	private String sort;
	/**
	 * 排序列
	 */
	private String col;

	/**
	 * 检索条件 计划编号
	 */
	private String planNidSrch;
	/**
	 * 检索条件 计划类型  “配置中心-汇添金配置”中所有计划类型（不区分启用禁用）
	 */
	private String planTypeSrch;

	/**
	 * 检索条件 用户名
	 */
	private String userName;
	/**
	 * 检索条件 项目编号
	 */
	private String borrowNid;
	/**
	 * 检索条件 项目类型
	 */
	private String projectType;
	/**
	 * 检索条件 计划订单号
	 */
	private String planOrderId;
	/**
	 * 检索条件 出借/承接订单号
	 */
	private String orderId;
	/**
	 * 检索条件 出借时间开始
	 */
	private String investTimeStart;
	/**
	 * 检索条件 出借时间结束
	 */
	private String investTimeEnd;
	/**
	 * 检索条件 limitStart
	 */
	private int limitStart = -1;
	/**
	 * 检索条件 limitEnd
	 */
	private int limitEnd = -1;
	public String getSort() {
		return sort;
	}
	public void setSort(String sort) {
		this.sort = sort;
	}
	public String getCol() {
		return col;
	}
	public void setCol(String col) {
		this.col = col;
	}
	public String getPlanNidSrch() {
		return planNidSrch;
	}
	public void setPlanNidSrch(String planNidSrch) {
		this.planNidSrch = planNidSrch;
	}
	public String getPlanTypeSrch() {
		return planTypeSrch;
	}
	public void setPlanTypeSrch(String planTypeSrch) {
		this.planTypeSrch = planTypeSrch;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getBorrowNid() {
		return borrowNid;
	}
	public void setBorrowNid(String borrowNid) {
		this.borrowNid = borrowNid;
	}
	public String getProjectType() {
		return projectType;
	}
	public void setProjectType(String projectType) {
		this.projectType = projectType;
	}
	public String getPlanOrderId() {
		return planOrderId;
	}
	public void setPlanOrderId(String planOrderId) {
		this.planOrderId = planOrderId;
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
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public String getInvestTimeStart() {
		return investTimeStart;
	}
	public void setInvestTimeStart(String investTimeStart) {
		this.investTimeStart = investTimeStart;
	}
	public String getInvestTimeEnd() {
		return investTimeEnd;
	}
	public void setInvestTimeEnd(String investTimeEnd) {
		this.investTimeEnd = investTimeEnd;
	}
	
}
