package com.hyjf.admin.manager.borrow.increaseinterest.investdetail;

import java.util.List;

import com.hyjf.common.paginator.Paginator;
import com.hyjf.mybatis.model.auto.IncreaseInterestInvest;

public class IncreaseInterestInvestDetailBean extends IncreaseInterestInvest {

	/**
	 * @Fields serialVersionUID
	 */
	private static final long serialVersionUID = -5230644124135606360L;

	/** 检索结果 */
	private List<IncreaseInterestInvest> recordList;

	/** 用户名(检索用) */
	private String userNameSrch;

	/** 项目编号(检索用) */
	private String borrowNidSrch;

	/** 出借开始时间(检索用) */
	private String timeStartSrch;

	/** 出借结束时间(检索用) */
	private String timeEndSrch;

	/**
	 * 翻页机能用的隐藏变量
	 */
	private int paginatorPage = 1;

	/**
	 * 列表画面自定义标签上的用翻页对象：paginator
	 */
	private Paginator paginator;

	public int getPaginatorPage() {
		return paginatorPage == 0 ? 1 : paginatorPage;
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

	public String getUserNameSrch() {
		return userNameSrch;
	}

	public void setUserNameSrch(String userNameSrch) {
		this.userNameSrch = userNameSrch;
	}

	public String getBorrowNidSrch() {
		return borrowNidSrch;
	}

	public void setBorrowNidSrch(String borrowNidSrch) {
		this.borrowNidSrch = borrowNidSrch;
	}

	public String getTimeStartSrch() {
		return timeStartSrch;
	}

	public void setTimeStartSrch(String timeStartSrch) {
		this.timeStartSrch = timeStartSrch;
	}

	public String getTimeEndSrch() {
		return timeEndSrch;
	}

	public void setTimeEndSrch(String timeEndSrch) {
		this.timeEndSrch = timeEndSrch;
	}

	public List<IncreaseInterestInvest> getRecordList() {
		return recordList;
	}

	public void setRecordList(List<IncreaseInterestInvest> recordList) {
		this.recordList = recordList;
	}

}
