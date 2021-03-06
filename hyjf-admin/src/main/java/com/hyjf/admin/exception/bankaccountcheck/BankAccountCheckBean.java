package com.hyjf.admin.exception.bankaccountcheck;

import java.io.Serializable;
import java.util.List;

import com.hyjf.common.paginator.Paginator;
import com.hyjf.mybatis.model.auto.AccountTrade;
import com.hyjf.mybatis.model.customize.admin.AdminBankAccountCheckCustomize;

public class BankAccountCheckBean extends AdminBankAccountCheckCustomize implements Serializable{

	private static final long serialVersionUID = -194123703827021279L;

	private List<AccountTrade> tradeList;
	
	private List<AdminBankAccountCheckCustomize> accountCheckList;
	
	
	/**
	 * 翻页机能用的隐藏变量
	 */
	private int paginatorPage = 1;
	/**
	 * 列表画面自定义标签上的用翻页对象：paginator
	 */
	private Paginator paginator;
	
	
	public List<AdminBankAccountCheckCustomize> getAccountCheckList() {
		return accountCheckList;
	}
	public void setAccountCheckList(List<AdminBankAccountCheckCustomize> accountCheckList) {
		this.accountCheckList = accountCheckList;
	}
	public List<AccountTrade> getTradeList() {
		return tradeList;
	}
	public void setTradeList(List<AccountTrade> tradeList) {
		this.tradeList = tradeList;
	}
	public int getPaginatorPage() {
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
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	
}
