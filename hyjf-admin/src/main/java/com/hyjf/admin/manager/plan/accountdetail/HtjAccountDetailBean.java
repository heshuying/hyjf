package com.hyjf.admin.manager.plan.accountdetail;

import java.io.Serializable;
import java.util.List;

import com.hyjf.common.paginator.Paginator;
import com.hyjf.mybatis.model.auto.AccountTrade;
import com.hyjf.mybatis.model.customize.admin.htj.DebtAccountDetailCustomize;

public class HtjAccountDetailBean extends DebtAccountDetailCustomize implements Serializable {

	/**
	 * serialVersionUID
	 */

	private static final long serialVersionUID = -3526461891770837162L;

	private List<DebtAccountDetailCustomize> recordList;

	private List<AccountTrade> tradeList;
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

	public List<DebtAccountDetailCustomize> getRecordList() {
		return recordList;
	}

	public void setRecordList(List<DebtAccountDetailCustomize> recordList) {
		this.recordList = recordList;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public List<AccountTrade> getTradeList() {
		return tradeList;
	}

	public void setTradeList(List<AccountTrade> tradeList) {
		this.tradeList = tradeList;
	}

}
