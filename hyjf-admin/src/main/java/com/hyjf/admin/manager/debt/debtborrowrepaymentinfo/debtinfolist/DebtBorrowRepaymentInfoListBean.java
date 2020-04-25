package com.hyjf.admin.manager.debt.debtborrowrepaymentinfo.debtinfolist;

import java.io.Serializable;

import com.hyjf.common.paginator.Paginator;
import com.hyjf.mybatis.model.customize.BorrowRepaymentInfoListCustomize;

public class DebtBorrowRepaymentInfoListBean extends BorrowRepaymentInfoListCustomize implements Serializable {

	/**
	 * serialVersionUID:TODO 这个变量是干什么的
	 */

	private static final long serialVersionUID = 1L;
	/**
	 * 状态 0为还款中,1为已还款
	 */
	private String status;
	/**
	 * 来自哪个Controller,0,1等
	 */
	private String actfrom;

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

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getActfrom() {
		return actfrom;
	}

	public void setActfrom(String actfrom) {
		this.actfrom = actfrom;
	}

}
