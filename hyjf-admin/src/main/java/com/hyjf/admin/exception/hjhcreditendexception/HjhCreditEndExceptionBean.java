package com.hyjf.admin.exception.hjhcreditendexception;

import java.io.Serializable;
import java.util.List;

import com.hyjf.common.paginator.Paginator;
import com.hyjf.mybatis.model.customize.BorrowInvestCustomize;
import com.hyjf.mybatis.model.customize.admin.AdminPlanAccedeListCustomize;

public class HjhCreditEndExceptionBean implements Serializable {

	/**
	 * @Fields serialVersionUID
	 */
	private static final long serialVersionUID = 936711485188059822L;
	
	/**
	 * 计划编号（检索）
	 */
	private String debtPlanNidSrch;
	/**
	 * 加入订单号（检索）
	 */
	private String accedeOrderIdSrch;
	/**
	 * 用户名（检索）
	 */
	private String userNameSrch;
	/**
	 * 推荐人用户名（检索）
	 */
	private String refereeNameSrch;
	/**
	 * 订单状态下拉框（检索）
	 */
	private String orderStatus;
	/**
	 * 平台下拉框（检索）
	 */
	private String platformSrch;
	/**
	 * 检索开始时间（画面推送开始时间）
	 */
	private String searchStartDate;
	/**
	 * 检索结束时间（画面推送结束时间）
	 */
	private String searchEndDate;
	/**
	 * 列表数据
	 */
	private List<AdminPlanAccedeListCustomize> recordList;
	/**
	 * 列表数据
	 */
	private List<BorrowInvestCustomize> recordLists;
	/**
	 * 翻页机能用的隐藏变量
	 */
	private int paginatorPage = 1;

	/**
	 * 检索条件 limitStart
	 */
	private int limitStart = -1;
	/**
	 * 检索条件 limitEnd
	 */
	private int limitEnd = -1;

	/**
	 * 列表画面自定义标签上的用翻页对象：paginator
	 */
	private Paginator paginator;

	public String getDebtPlanNidSrch() {
		return debtPlanNidSrch;
	}

	public void setDebtPlanNidSrch(String debtPlanNidSrch) {
		this.debtPlanNidSrch = debtPlanNidSrch;
	}

	public String getAccedeOrderIdSrch() {
		return accedeOrderIdSrch;
	}

	public void setAccedeOrderIdSrch(String accedeOrderIdSrch) {
		this.accedeOrderIdSrch = accedeOrderIdSrch;
	}

	public String getUserNameSrch() {
		return userNameSrch;
	}

	public void setUserNameSrch(String userNameSrch) {
		this.userNameSrch = userNameSrch;
	}

	public String getRefereeNameSrch() {
		return refereeNameSrch;
	}

	public void setRefereeNameSrch(String refereeNameSrch) {
		this.refereeNameSrch = refereeNameSrch;
	}

	public String getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}

	public String getPlatformSrch() {
		return platformSrch;
	}

	public void setPlatformSrch(String platformSrch) {
		this.platformSrch = platformSrch;
	}

	public String getSearchStartDate() {
		return searchStartDate;
	}

	public void setSearchStartDate(String searchStartDate) {
		this.searchStartDate = searchStartDate;
	}

	public String getSearchEndDate() {
		return searchEndDate;
	}

	public void setSearchEndDate(String searchEndDate) {
		this.searchEndDate = searchEndDate;
	}

	public List<AdminPlanAccedeListCustomize> getRecordList() {
		return recordList;
	}

	public void setRecordList(List<AdminPlanAccedeListCustomize> recordList) {
		this.recordList = recordList;
	}

	public int getPaginatorPage() {
		return paginatorPage;
	}

	public void setPaginatorPage(int paginatorPage) {
		this.paginatorPage = paginatorPage;
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

	public Paginator getPaginator() {
		return paginator;
	}

	public void setPaginator(Paginator paginator) {
		this.paginator = paginator;
	}

	public List<BorrowInvestCustomize> getRecordLists() {
		return recordLists;
	}

	public void setRecordLists(List<BorrowInvestCustomize> recordLists) {
		this.recordLists = recordLists;
	}

}
