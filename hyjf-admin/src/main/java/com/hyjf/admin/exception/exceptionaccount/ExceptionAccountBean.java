package com.hyjf.admin.exception.exceptionaccount;

import java.io.Serializable;
import java.util.List;

import com.hyjf.common.paginator.Paginator;
import com.hyjf.mybatis.model.auto.ExceptionAccount;

public class ExceptionAccountBean extends ExceptionAccount implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private List<ExceptionAccount> recordList;

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

	public List<ExceptionAccount> getRecordList() {
		return recordList;
	}

	public void setRecordList(List<ExceptionAccount> recordList) {
		this.recordList = recordList;
	}

}
