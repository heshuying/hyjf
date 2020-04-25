package com.hyjf.admin.manager.user.bankcard;

import java.io.Serializable;
import java.util.List;

import com.hyjf.common.paginator.Paginator;
import com.hyjf.mybatis.model.customize.admin.AdminBankcardListCustomize;

public class BankcardListCustomizeBean extends AdminBankcardListCustomize implements Serializable {

	/**
	 * serialVersionUID
	 */

	private static final long serialVersionUID = 387630498860089653L;

	private List<AdminBankcardListCustomize> recordList;

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

	public Paginator getPaginator() {
		return paginator;
	}

	public void setPaginator(Paginator paginator) {
		this.paginator = paginator;
	}

	public List<AdminBankcardListCustomize> getRecordList() {
		return recordList;
	}

	public void setRecordList(List<AdminBankcardListCustomize> recordList) {
		this.recordList = recordList;
	}

	/**
	 * addTimeStart
	 * 
	 * @return the addTimeStart
	 */

	public String getAddTimeStart() {
		return addTimeStart;
	}

	/**
	 * @param addTimeStart
	 *            the addTimeStart to set
	 */

	public void setAddTimeStart(String addTimeStart) {
		this.addTimeStart = addTimeStart;
	}

	/**
	 * addTimeEnd
	 * 
	 * @return the addTimeEnd
	 */

	public String getAddTimeEnd() {
		return addTimeEnd;
	}

	/**
	 * @param addTimeEnd
	 *            the addTimeEnd to set
	 */

	public void setAddTimeEnd(String addTimeEnd) {
		this.addTimeEnd = addTimeEnd;
	}

}
