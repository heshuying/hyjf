/**
 * Description:用户开户记录列表前端查询所用vo
 * Copyright: Copyright (HYJF Corporation) 2015
 * Company: HYJF Corporation
 * @author: 王坤
 * @version: 1.0
 * Created at: 2015年11月11日 下午2:17:31
 * Modification History:
 * Modified by : 
 */

package com.hyjf.admin.manager.user.account;

import java.io.Serializable;
import java.util.List;

import com.hyjf.common.paginator.Paginator;
import com.hyjf.mybatis.model.customize.admin.AdminAccountListCustomize;

/**
 * @author 王坤
 */

public class AccountListCustomizeBean extends AdminAccountListCustomize implements Serializable {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 7825037968017727380L;

	private List<AdminAccountListCustomize> recordList;
	private String openTimeStart;
	private String openTimeEnd;

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

	public List<AdminAccountListCustomize> getRecordList() {
		return recordList;
	}

	public void setRecordList(List<AdminAccountListCustomize> recordList) {
		this.recordList = recordList;
	}

	/**
	 * openTimeStart
	 * 
	 * @return the openTimeStart
	 */

	public String getOpenTimeStart() {
		return openTimeStart;
	}

	/**
	 * @param openTimeStart
	 *            the openTimeStart to set
	 */

	public void setOpenTimeStart(String openTimeStart) {
		this.openTimeStart = openTimeStart;
	}

	/**
	 * openTimeEnd
	 * 
	 * @return the openTimeEnd
	 */

	public String getOpenTimeEnd() {
		return openTimeEnd;
	}

	/**
	 * @param openTimeEnd
	 *            the openTimeEnd to set
	 */

	public void setOpenTimeEnd(String openTimeEnd) {
		this.openTimeEnd = openTimeEnd;
	}

}
