package com.hyjf.admin.finance.accountmanage;

import java.io.Serializable;
import java.util.List;

import com.hyjf.common.paginator.Paginator;
import com.hyjf.mybatis.model.customize.AccountManageCustomize;

public class AccountManageBean extends AccountManageCustomize implements Serializable {

	/**
	 * serialVersionUID:
	 */

	private static final long serialVersionUID = 2561663838042185968L;

	private List<AccountManageCustomize> recordList;
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

	public List<AccountManageCustomize> getRecordList() {
		return recordList;
	}

	public void setRecordList(List<AccountManageCustomize> recordList) {
		this.recordList = recordList;
	}



	public static long getSerialversionuid() {
		return serialVersionUID;
	}



}















