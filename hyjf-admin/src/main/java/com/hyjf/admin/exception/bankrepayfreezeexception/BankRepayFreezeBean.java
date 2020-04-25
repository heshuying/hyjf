package com.hyjf.admin.exception.bankrepayfreezeexception;

import com.hyjf.common.paginator.Paginator;
import com.hyjf.mybatis.model.auto.BankRepayFreezeLog;

import java.io.Serializable;
import java.util.List;

public class BankRepayFreezeBean extends BankRepayFreezeLog implements Serializable {

	/**
	 * serialVersionUID:
	 */

	private static final long serialVersionUID = 2561663838042185968L;

	private List<BankRepayFreezeLog> recordList;
	/**
	 * 翻页机能用的隐藏变量
	 */
	private int paginatorPage = 1;

	/**
	 * 列表画面自定义标签上的用翻页对象：paginator
	 */
	private Paginator paginator;

	private String orderId;

	private String submitTimeStartSrch;

	private String submitTimeEndSrch;

	private String orderIdSrch;

	private int limitStart = -1;

	private int limitEnd = -1;

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

	public List<BankRepayFreezeLog> getRecordList() {
		return recordList;
	}

	public void setRecordList(List<BankRepayFreezeLog> recordList) {
		this.recordList = recordList;
	}

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

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getSubmitTimeStartSrch() {
		return submitTimeStartSrch;
	}

	public void setSubmitTimeStartSrch(String submitTimeStartSrch) {
		this.submitTimeStartSrch = submitTimeStartSrch;
	}

	public String getSubmitTimeEndSrch() {
		return submitTimeEndSrch;
	}

	public void setSubmitTimeEndSrch(String submitTimeEndSrch) {
		this.submitTimeEndSrch = submitTimeEndSrch;
	}

	public String getOrderIdSrch() {
		return orderIdSrch;
	}

	public void setOrderIdSrch(String orderIdSrch) {
		this.orderIdSrch = orderIdSrch;
	}
}
