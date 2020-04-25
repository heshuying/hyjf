package com.hyjf.admin.manager.user.userauth;

import com.hyjf.common.paginator.Paginator;
import com.hyjf.mybatis.model.customize.admin.AdminUserAuthListCustomize;

import java.io.Serializable;
import java.util.List;

public class UserauthListCustomizeBean extends AdminUserAuthListCustomize implements Serializable {

	/**
	 * serialVersionUID
	 */

	private static final long serialVersionUID = 387630498860089653L;

	private List<AdminUserAuthListCustomize> recordList;

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

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	public List<AdminUserAuthListCustomize> getRecordList() {
		return recordList;
	}

	public Paginator getPaginator() {

		return paginator;
	}

	public void setPaginator(Paginator paginator) {
		this.paginator = paginator;
	}

	public void setRecordList(List<AdminUserAuthListCustomize> recordList) {
		this.recordList = recordList;
	}
}
