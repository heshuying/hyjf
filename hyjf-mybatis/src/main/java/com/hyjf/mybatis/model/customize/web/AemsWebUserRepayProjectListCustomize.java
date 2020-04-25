/**
 * Description:用户开户列表前端显示查询所用po
 * Copyright: Copyright (HYJF Corporation) 2015
 * Company: HYJF Corporation
 * @author: 王坤
 * @version: 1.0
 *           Created at: 2015年11月11日 下午2:17:31
 *           Modification History:
 *           Modified by :
 */

package com.hyjf.mybatis.model.customize.web;

import java.io.Serializable;

/**
 * @author 王坤
 */

public class AemsWebUserRepayProjectListCustomize implements Serializable {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 5429348260157960848L;

	// 项目编号
	private String borrowNid;

	// 项目年华收益率
	private String borrowInterest;

	// 借款金额
	private String borrowAccount;

	/**
	 * 到账金额
	 */
	private String yesAccount;
	/**
	 * 到账时间
	 */
	private String yesAccountTime;
	// 还款金额
	private String borrowTotal;

	// 实际还款时间
	private String repayYesTime;
	//实际(应)还款利息
	private String repayInterest;
	//实际(应)还款本金
	private String repayCapital;

	public String getBorrowNid() {
		return borrowNid;
	}

	public void setBorrowNid(String borrowNid) {
		this.borrowNid = borrowNid;
	}

	public String getBorrowInterest() {
		return borrowInterest;
	}

	public void setBorrowInterest(String borrowInterest) {
		this.borrowInterest = borrowInterest;
	}

	public String getBorrowAccount() {
		return borrowAccount;
	}

	public void setBorrowAccount(String borrowAccount) {
		this.borrowAccount = borrowAccount;
	}

	public String getYesAccount() {
		return yesAccount;
	}

	public void setYesAccount(String yesAccount) {
		this.yesAccount = yesAccount;
	}

	public String getYesAccountTime() {
		return yesAccountTime;
	}

	public void setYesAccountTime(String yesAccountTime) {
		this.yesAccountTime = yesAccountTime;
	}

	public String getBorrowTotal() {
		return borrowTotal;
	}

	public void setBorrowTotal(String borrowTotal) {
		this.borrowTotal = borrowTotal;
	}

	public String getRepayYesTime() {
		return repayYesTime;
	}

	public void setRepayYesTime(String repayYesTime) {
		this.repayYesTime = repayYesTime;
	}

	public String getRepayInterest() {
		return repayInterest;
	}

	public void setRepayInterest(String repayInterest) {
		this.repayInterest = repayInterest;
	}

	public String getRepayCapital() {
		return repayCapital;
	}

	public void setRepayCapital(String repayCapital) {
		this.repayCapital = repayCapital;
	}
}
