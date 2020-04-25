package com.hyjf.admin.manager.borrow.increaseinterest.repaydetail.infolist;

import java.io.Serializable;
import java.util.List;

import com.hyjf.common.paginator.Paginator;
import com.hyjf.mybatis.model.customize.admin.AdminIncreaseInterestRepayCustomize;

/**
 * 融通宝加息还款明细详情Bean
 * 
 * @ClassName IncreaseInterestRepayInfoListBean
 * @author liuyang
 * @date 2017年1月4日 下午5:05:14
 */
public class IncreaseInterestRepayInfoListBean implements Serializable {

	/**
	 * @Fields serialVersionUID
	 */
	private static final long serialVersionUID = -8255027462701168745L;

	/**
	 * 状态 0为还款中,1为已还款
	 */
	private String status;
	/**
	 * 来自哪个Controller,0,1等
	 */
	private String actfrom;

	/**
	 * 翻页机能用的隐藏变量
	 */
	private int paginatorPage = 1;

	/** 检索结果 */
	private List<AdminIncreaseInterestRepayCustomize> recordList;

	/** 项目编号(检索用) */
	private String borrowNidSrch;

	/** 用户名(检索用) */
	private String userNameSrch;

	/** 出借开始时间(检索用) */
	private String timeStartSrch;

	/** 出借结束时间(检索用) */
	private String timeEndSrch;

	/** 出借Id(检索用) */
	private String investIdSrch;

	/** 还款期数(检索用) */
	private String repayPeriodSrch;
	/**
	 * 列表画面自定义标签上的用翻页对象：paginator
	 */
	private Paginator paginator;

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getActfrom() {
		return actfrom;
	}

	public void setActfrom(String actfrom) {
		this.actfrom = actfrom;
	}

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

	public List<AdminIncreaseInterestRepayCustomize> getRecordList() {
		return recordList;
	}

	public void setRecordList(List<AdminIncreaseInterestRepayCustomize> recordList) {
		this.recordList = recordList;
	}

	public String getBorrowNidSrch() {
		return borrowNidSrch;
	}

	public void setBorrowNidSrch(String borrowNidSrch) {
		this.borrowNidSrch = borrowNidSrch;
	}

	public String getUserNameSrch() {
		return userNameSrch;
	}

	public void setUserNameSrch(String userNameSrch) {
		this.userNameSrch = userNameSrch;
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

	public String getInvestIdSrch() {
		return investIdSrch;
	}

	public void setInvestIdSrch(String investIdSrch) {
		this.investIdSrch = investIdSrch;
	}

	public String getRepayPeriodSrch() {
		return repayPeriodSrch;
	}

	public void setRepayPeriodSrch(String repayPeriodSrch) {
		this.repayPeriodSrch = repayPeriodSrch;
	}

}
