package com.hyjf.admin.manager.plan.joindetail;

import java.io.Serializable;
import java.util.List;

import com.hyjf.common.paginator.Paginator;
import com.hyjf.mybatis.model.customize.admin.AdminPlanAccedeDetailCustomize;

public class HtjJoinDetailBean implements Serializable {

	/**
	 * @Fields serialVersionUID
	 */
	private static final long serialVersionUID = 8083755666574893764L;

	/**
	 * 计划编号
	 */
	private String debtPlanNidSrch;

	/**
	 * 加入订单号
	 */
	private String accedeOrderIdSrch;
	/**
	 * 冻结订单号
	 */
	private String freezeOrderIdSrch;

	/**
	 * 用户名
	 */
	private String userNameSrch;

	/**
	 * 推荐人用户名
	 */
	private String refereeNameSrch;

	/**
	 * 用户属性
	 */
	private String userAttributeSrch;

	/**
	 * 平台
	 */
	private String platformSrch;

	/**
	 * 检索开始时间
	 */
	private String searchStartDate;

	/**
	 * 检索结束时间
	 */
	private String searchEndDate;

	/**
	 * 列表数据
	 */
	private List<AdminPlanAccedeDetailCustomize> recordList;

	/**
	 * 总计
	 */
	private String total;

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

	public int getPaginatorPage() {
		if (paginatorPage == 0) {
			paginatorPage = 1;
		}
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

	public String getFreezeOrderIdSrch() {
		return freezeOrderIdSrch;
	}

	public void setFreezeOrderIdSrch(String freezeOrderIdSrch) {
		this.freezeOrderIdSrch = freezeOrderIdSrch;
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

	public String getUserAttributeSrch() {
		return userAttributeSrch;
	}

	public void setUserAttributeSrch(String userAttributeSrch) {
		this.userAttributeSrch = userAttributeSrch;
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

	public List<AdminPlanAccedeDetailCustomize> getRecordList() {
		return recordList;
	}

	public void setRecordList(List<AdminPlanAccedeDetailCustomize> recordList) {
		this.recordList = recordList;
	}

	public String getTotal() {
		return total;
	}

	public void setTotal(String total) {
		this.total = total;
	}

}
