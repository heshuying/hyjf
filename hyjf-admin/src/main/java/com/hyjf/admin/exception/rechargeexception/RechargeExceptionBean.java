package com.hyjf.admin.exception.rechargeexception;

import java.io.Serializable;
import java.util.List;

import com.hyjf.common.paginator.Paginator;
import com.hyjf.mybatis.model.auto.BankConfig;
import com.hyjf.mybatis.model.customize.RechargeCustomize;

public class RechargeExceptionBean extends RechargeCustomize implements Serializable {

	/**
	 * serialVersionUID:
	 */
		
	private static final long serialVersionUID = 2561663838042185965L;
	
	private String username;//用户名
	private String truename;//用户真实姓名
	private String password; //手动充值密码

	
	private List<RechargeCustomize> recordList;
	
	private List<BankConfig> bankList;
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

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getTruename() {
		return truename;
	}

	public void setTruename(String truename) {
		this.truename = truename;
	}

	public List<RechargeCustomize> getRecordList() {
		return recordList;
	}

	public void setRecordList(List<RechargeCustomize> recordList) {
		this.recordList = recordList;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public List<BankConfig> getBankList() {
		return bankList;
	}

	public void setBankList(List<BankConfig> bankList) {
		this.bankList = bankList;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}


}















	