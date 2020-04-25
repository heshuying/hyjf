package com.hyjf.bank.service.user.repay;

import com.hyjf.mybatis.model.auto.CreditRepay;
import com.hyjf.mybatis.model.auto.HjhDebtCreditRepay;

import java.io.Serializable;
import java.math.BigDecimal;

public class HjhDebtCreditRepayBean extends HjhDebtCreditRepay implements Serializable {

	/**
	 * 序列化id
	 */
	private static final long serialVersionUID = -6191363977885745665L;
	private BigDecimal assignTotal;

	public BigDecimal getAssignTotal() {
		return assignTotal;
	}

	public void setAssignTotal(BigDecimal assignTotal) {
		this.assignTotal = assignTotal;
	}
	


}
