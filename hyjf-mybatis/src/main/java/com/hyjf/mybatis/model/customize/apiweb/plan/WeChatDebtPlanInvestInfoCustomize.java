package com.hyjf.mybatis.model.customize.apiweb.plan;

import java.io.Serializable;

public class WeChatDebtPlanInvestInfoCustomize implements Serializable {

	private static final long serialVersionUID = -6211844409789438827L;

	// 可用余额
	private String balance;
	// 起投金额
	private String debtMinInvestment;
	// 递增金额
	private String debtInvestmentIncrement;
	// 剩余可投金额
	private String planAccountWait;

	public String getBalance() {
		return balance;
	}

	public void setBalance(String balance) {
		this.balance = balance;
	}

	public String getDebtMinInvestment() {
		return debtMinInvestment;
	}

	public void setDebtMinInvestment(String debtMinInvestment) {
		this.debtMinInvestment = debtMinInvestment;
	}

	public String getDebtInvestmentIncrement() {
		return debtInvestmentIncrement;
	}

	public void setDebtInvestmentIncrement(String debtInvestmentIncrement) {
		this.debtInvestmentIncrement = debtInvestmentIncrement;
	}

	public String getPlanAccountWait() {
		return planAccountWait;
	}

	public void setPlanAccountWait(String planAccountWait) {
		this.planAccountWait = planAccountWait;
	}

}
