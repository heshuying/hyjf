package com.hyjf.admin.manager.plan.creditdetail;

import java.io.Serializable;

import com.hyjf.common.paginator.Paginator;

/**
 * @package com.hyjf.admin.maintenance.AlllplanCustomize;
 * @author GOGTZ-Z
 * @date 2015/07/09 17:00
 * @version V1.0  
 */
public class CreditDetailBean implements Serializable {

	/**
	 * serialVersionUID
	 */

	private static final long serialVersionUID = 387630498860089653L;

	/**
	 * 排序
	 */
	private String sort;
	/**
	 * 排序列
	 */
	private String col;

	/**
	 * 翻页机能用的隐藏变量
	 */
	private int paginatorPage = 1;

	/**
	 * 列表画面自定义标签上的用翻页对象：paginator
	 */
	private Paginator paginator;

	/**
	 * 检索条件 计划编号
	 */
	private String planNidSrch;
	/**
	 * 检索条件 用户名
	 */
	private String userName;
	/**
	 * 检索条件 出借时间开始
	 */
	private String investTimeStart;
	/**
	 * 检索条件 出借时间结束
	 */
	private String investTimeEnd;
	/**
	 * 检索条件 项目编号
	 */
	private String borrowNidSrch;
	/**
	 * 检索条件 项目类型
	 */
	private String projectTypeSrch;
	/**
	 * 检索条件 计划订单号
	 */
	private String planOrderId;
	/**
	 * 检索条件 出借/承接订单号
	 */
	private String orderId;
	
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
	public int getPaginatorPage() {
		return paginatorPage;
	}
	public void setPaginatorPage(int paginatorPage) {
		this.paginatorPage = paginatorPage;
	}
	public Paginator getPaginator() {
		return paginator;
	}
	public void setPaginator(Paginator paginator) {
		this.paginator = paginator;
	}
	public String getPlanNidSrch() {
		return planNidSrch;
	}
	public void setPlanNidSrch(String planNidSrch) {
		this.planNidSrch = planNidSrch;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
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
	public String getBorrowNidSrch() {
		return borrowNidSrch;
	}
	public void setBorrowNidSrch(String borrowNidSrch) {
		this.borrowNidSrch = borrowNidSrch;
	}
	public String getProjectTypeSrch() {
		return projectTypeSrch;
	}
	public void setProjectTypeSrch(String projectTypeSrch) {
		this.projectTypeSrch = projectTypeSrch;
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

}
