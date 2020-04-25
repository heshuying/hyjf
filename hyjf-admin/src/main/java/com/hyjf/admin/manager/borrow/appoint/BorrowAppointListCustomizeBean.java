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

package com.hyjf.admin.manager.borrow.appoint;

import java.io.Serializable;
import java.util.List;

import com.hyjf.common.paginator.Paginator;
import com.hyjf.mybatis.model.customize.admin.AdminBorrowAppointCustomize;

/**
 * @author 王坤
 */

public class BorrowAppointListCustomizeBean extends AdminBorrowAppointCustomize implements Serializable {

	/**
	 * serialVersionUID:序列化id
	 */
	private static final long serialVersionUID = 6236456923128928996L;
	//预约记录
	private List<AdminBorrowAppointCustomize> recordList;
	//预约取消开始时间
	private String cancelTimeStart;
	//预约取消结束时间
	private String cancelTimeEnd;
	//预约开始时间
	private String appointTimeStart;
	//预约结束时间
	private String appointTimeEnd;

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

	public List<AdminBorrowAppointCustomize> getRecordList() {
		return recordList;
	}

	public void setRecordList(List<AdminBorrowAppointCustomize> recordList) {
		this.recordList = recordList;
	}

	public String getCancelTimeStart() {
		return cancelTimeStart;
	}

	public void setCancelTimeStart(String cancelTimeStart) {
		this.cancelTimeStart = cancelTimeStart;
	}

	public String getCancelTimeEnd() {
		return cancelTimeEnd;
	}

	public void setCancelTimeEnd(String cancelTimeEnd) {
		this.cancelTimeEnd = cancelTimeEnd;
	}

	public String getAppointTimeStart() {
		return appointTimeStart;
	}

	public void setAppointTimeStart(String appointTimeStart) {
		this.appointTimeStart = appointTimeStart;
	}

	public String getAppointTimeEnd() {
		return appointTimeEnd;
	}

	public void setAppointTimeEnd(String appointTimeEnd) {
		this.appointTimeEnd = appointTimeEnd;
	}



}
