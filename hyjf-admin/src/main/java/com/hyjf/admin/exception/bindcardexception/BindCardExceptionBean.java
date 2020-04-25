package com.hyjf.admin.exception.bindcardexception;

import java.io.Serializable;
import java.util.List;

import com.hyjf.common.paginator.Paginator;
import com.hyjf.mybatis.model.customize.BindCardExceptionCustomize;

public class BindCardExceptionBean implements Serializable {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 6378059195675183813L;
	/**
	 * 用户ID
	 */
	private Integer userId;
	/**
	 * 电子账户号
	 */
	private String accountId;
	/**
	 * 用户名(检索用)
	 */
	private String userNameSrch;

	/**
	 * 电子账户号(检索用)
	 */
	private String accountIdSrch;

	private List<BindCardExceptionCustomize> recordList;

	/**
	 * 翻页机能用的隐藏变量
	 */
	private int paginatorPage = 1;

	/**
	 * 列表画面自定义标签上的用翻页对象：paginator
	 */
	private Paginator paginator;

	/**
	 * 检索条件 limitStart
	 */
	private int limitStart = -1;
	/**
	 * 检索条件 limitEnd
	 */
	private int limitEnd = -1;

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

	public String getUserNameSrch() {
		return userNameSrch;
	}

	public void setUserNameSrch(String userNameSrch) {
		this.userNameSrch = userNameSrch;
	}

	public String getAccountIdSrch() {
		return accountIdSrch;
	}

	public void setAccountIdSrch(String accountIdSrch) {
		this.accountIdSrch = accountIdSrch;
	}

	public List<BindCardExceptionCustomize> getRecordList() {
		return recordList;
	}

	public void setRecordList(List<BindCardExceptionCustomize> recordList) {
		this.recordList = recordList;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getAccountId() {
		return accountId;
	}

	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}

}
