package com.hyjf.admin.manager.debt.debtborrowrepayment;

import java.io.Serializable;

import com.hyjf.mybatis.model.customize.admin.AdminRepayDelayCustomize;

public class DebtRepayDelayCustomizeBean extends AdminRepayDelayCustomize implements Serializable {

	private static final long serialVersionUID = 1129167159201474256L;

	private String nid;

	private String repayTime;

	/**
	 * repayTime
	 * 
	 * @return the repayTime
	 */

	public String getRepayTime() {
		return repayTime;
	}

	/**
	 * @param repayTime
	 *            the repayTime to set
	 */

	public void setRepayTime(String repayTime) {
		this.repayTime = repayTime;
	}

	/**
	 * nid
	 * 
	 * @return the nid
	 */

	public String getNid() {
		return nid;
	}

	/**
	 * @param nid
	 *            the nid to set
	 */

	public void setNid(String nid) {
		this.nid = nid;
	}

	public String delayDays;// 延期天数

	public DebtRepayDelayCustomizeBean() {
		super();
	}

	public String getDelayDays() {
		return delayDays;
	}

	public void setDelayDays(String delayDays) {
		this.delayDays = delayDays;
	}

}
