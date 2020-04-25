package com.hyjf.admin.manager.user.bankcardlog;

import java.io.Serializable;
import java.util.List;

import com.hyjf.common.paginator.Paginator;
import com.hyjf.mybatis.model.auto.BankAccountLog;

/**
 * 用户绑卡操作记录
 * @author Michael
 */
public class BankAccountLogBean extends BankAccountLog implements Serializable {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 3803722754627032581L;

	/**
	 * 前台时间接收
	 */
	private String startTime;
	private String endTime;
	/**
	 * 用户名搜索
	 */
	private String userNameSrch;
	/**
	 * 银行搜索
	 */
	private String bankIdSrch;


	private List<BankAccountLog> recordList;

	/**
	 * 翻页机能用的隐藏变量
	 */
	private int paginatorPage = 1;

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

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public String getUserNameSrch() {
		return userNameSrch;
	}

	public void setUserNameSrch(String userNameSrch) {
		this.userNameSrch = userNameSrch;
	}

	public String getBankIdSrch() {
		return bankIdSrch;
	}

	public void setBankIdSrch(String bankIdSrch) {
		this.bankIdSrch = bankIdSrch;
	}

	public List<BankAccountLog> getRecordList() {
		return recordList;
	}

	public void setRecordList(List<BankAccountLog> recordList) {
		this.recordList = recordList;
	}

}
