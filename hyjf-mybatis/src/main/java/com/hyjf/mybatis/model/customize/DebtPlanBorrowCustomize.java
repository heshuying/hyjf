package com.hyjf.mybatis.model.customize;

import java.io.Serializable;
import java.util.List;

/**
 * 汇添金关联资产Bean
 * 
 * @ClassName DebtPlanBorrowCustomize
 * @author liuyang
 * @date 2016年9月23日 上午9:14:35
 */
public class DebtPlanBorrowCustomize implements Serializable {

	/**
	 * @Fields serialVersionUID
	 */
	private static final long serialVersionUID = -1276346877984247211L;

	/**
	 * 是否选择
	 */
	private String isSelected;

	/**
	 * 计划编号
	 */
	private String debtPlanNid;

	/**
	 * 项目编号
	 */
	private String borrowNid;

	/**
	 * 项目名称
	 */
	private String projectName;

	/**
	 * 项目金额
	 */
	private String account;

	/**
	 * 剩余可投
	 */
	private String borrowAccountWait;

	/**
	 * 项目期限
	 */
	private String borrowPeriod;

	/**
	 * 还款方式
	 */
	private String borrowStyle;

	/**
	 * 预期年化收益率
	 */
	private String borrowApr;

	/**
	 * 已关联计划编号
	 */
	private List<String> debtPlanNidList;

	public String getIsSelected() {
		return isSelected;
	}

	public void setIsSelected(String isSelected) {
		this.isSelected = isSelected;
	}

	public String getBorrowNid() {
		return borrowNid;
	}

	public void setBorrowNid(String borrowNid) {
		this.borrowNid = borrowNid;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getBorrowAccountWait() {
		return borrowAccountWait;
	}

	public void setBorrowAccountWait(String borrowAccountWait) {
		this.borrowAccountWait = borrowAccountWait;
	}

	public String getBorrowPeriod() {
		return borrowPeriod;
	}

	public void setBorrowPeriod(String borrowPeriod) {
		this.borrowPeriod = borrowPeriod;
	}

	public String getBorrowStyle() {
		return borrowStyle;
	}

	public void setBorrowStyle(String borrowStyle) {
		this.borrowStyle = borrowStyle;
	}

	public String getBorrowApr() {
		return borrowApr;
	}

	public void setBorrowApr(String borrowApr) {
		this.borrowApr = borrowApr;
	}

	public String getDebtPlanNid() {
		return debtPlanNid;
	}

	public void setDebtPlanNid(String debtPlanNid) {
		this.debtPlanNid = debtPlanNid;
	}

	public List<String> getDebtPlanNidList() {
		return debtPlanNidList;
	}

	public void setDebtPlanNidList(List<String> debtPlanNidList) {
		this.debtPlanNidList = debtPlanNidList;
	}

}
