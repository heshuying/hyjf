package com.hyjf.admin.manager.borrow.increaseinterest.repayplan;

import java.util.List;

import com.hyjf.common.paginator.Paginator;
import com.hyjf.mybatis.model.auto.IncreaseInterestRepayDetail;

/**
 * 融通宝加息还款计划
 * 
 * @ClassName IncreaseInterestRepayPlanBean
 * @author liuyang
 * @date 2016年12月29日 上午9:04:15
 */
public class IncreaseInterestRepayPlanBean extends IncreaseInterestRepayDetail {

	/**
	 * @Fields serialVersionUID
	 */
	private static final long serialVersionUID = -3139026392854557599L;

	/** 检索结果 */
	private List<IncreaseInterestRepayDetail> recordList;

	/** 项目编号(检索用) */
	private String borrowNidSrch;

	/** 还款状态(检索用) */
	private String repayStatusSrch;

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

	public List<IncreaseInterestRepayDetail> getRecordList() {
		return recordList;
	}

	public void setRecordList(List<IncreaseInterestRepayDetail> recordList) {
		this.recordList = recordList;
	}

	public String getBorrowNidSrch() {
		return borrowNidSrch;
	}

	public void setBorrowNidSrch(String borrowNidSrch) {
		this.borrowNidSrch = borrowNidSrch;
	}

	public String getRepayStatusSrch() {
		return repayStatusSrch;
	}

	public void setRepayStatusSrch(String repayStatusSrch) {
		this.repayStatusSrch = repayStatusSrch;
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

}
