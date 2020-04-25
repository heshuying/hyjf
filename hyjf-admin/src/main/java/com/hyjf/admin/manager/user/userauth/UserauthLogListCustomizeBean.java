package com.hyjf.admin.manager.user.userauth;

import com.hyjf.common.paginator.Paginator;
import com.hyjf.mybatis.model.customize.admin.AdminUserAuthLogListCustomize;

import java.io.Serializable;
import java.util.List;

public class UserauthLogListCustomizeBean extends AdminUserAuthLogListCustomize implements Serializable {

	/**
	 * serialVersionUID
	 */

	private static final long serialVersionUID = 387630498860089653L;

	private List<AdminUserAuthLogListCustomize> recordList;

	// 添加时机 开始
	public String addTimeStart;

	// 添加时间 结束
	public String addTimeEnd;

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

	public List<AdminUserAuthLogListCustomize> getRecordList() {
		return recordList;
	}

	public Paginator getPaginator() {

		return paginator;
	}

	public void setPaginator(Paginator paginator) {
		this.paginator = paginator;
	}

	public void setRecordList(List<AdminUserAuthLogListCustomize> recordList) {
		this.recordList = recordList;
	}

	public String getAddTimeStart() {
		return addTimeStart;
	}

	public void setAddTimeStart(String addTimeStart) {
		this.addTimeStart = addTimeStart;
	}

	public String getAddTimeEnd() {
		return addTimeEnd;
	}

	public void setAddTimeEnd(String addTimeEnd) {
		this.addTimeEnd = addTimeEnd;
	}
}
