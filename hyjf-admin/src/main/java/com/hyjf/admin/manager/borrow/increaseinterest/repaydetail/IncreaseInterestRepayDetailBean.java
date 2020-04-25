package com.hyjf.admin.manager.borrow.increaseinterest.repaydetail;

import java.util.List;

import com.hyjf.common.paginator.Paginator;
import com.hyjf.mybatis.model.auto.IncreaseInterestRepay;
import com.hyjf.mybatis.model.customize.admin.AdminIncreaseInterestRepayCustomize;

/**
 * 融通宝加息还款明细
 *
 * @ClassName IncreaseInterestRepayDetailBean
 * @author liuyang
 * @date 2016年12月29日 上午11:08:30
 */
public class IncreaseInterestRepayDetailBean extends IncreaseInterestRepay {
	/**
	 * @Fields serialVersionUID
	 */
	private static final long serialVersionUID = -6717073528703598667L;

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

	/**转账状态*/
	private String repayStatusSrch;

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

	public String getRepayStatusSrch() {
		return repayStatusSrch;
	}

	public void setRepayStatusSrch(String repayStatusSrch) {
		this.repayStatusSrch = repayStatusSrch;
	}
}
