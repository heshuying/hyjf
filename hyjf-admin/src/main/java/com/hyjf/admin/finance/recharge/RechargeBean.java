package com.hyjf.admin.finance.recharge;

import java.io.Serializable;
import java.util.List;

import com.hyjf.common.paginator.Paginator;
import com.hyjf.mybatis.model.auto.BanksConfig;
import com.hyjf.mybatis.model.customize.RechargeCustomize;

public class RechargeBean extends RechargeCustomize implements Serializable {

	/**
	 * serialVersionUID:
	 */

	private static final long serialVersionUID = 2561663838042185965L;

	private String username;// 用户名
	private String truename;// 用户真实姓名
	private String password; // 手动充值密码
	private String roleId; // 用户角色

	public String getRoleId() {
		return roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	private List<RechargeCustomize> recordList;

	private List<BanksConfig> banksList;
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

	public List<BanksConfig> getBanksList() {
		return banksList;
	}

	public void setBanksList(List<BanksConfig> banksList) {
		this.banksList = banksList;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
