/**
 * Description:（类功能描述-必填） 需要在每个方法前添加业务描述，方便业务业务行为的BI工作
 * Copyright: Copyright (HYJF Corporation)2015
 * Company: HYJF Corporation
 * @author: Administrator
 * @version: 1.0
 * Created at: 2015年12月16日 下午4:40:39
 * Modification History:
 * Modified by : 
 */

package com.hyjf.mybatis.model.customize;

import java.io.Serializable;

/**
 * @author Administrator
 */

public class ProjectTypeCustomize implements Serializable {

	/**
	 * serialVersionUID:
	 */

	private static final long serialVersionUID = 1L;
	private String borrowProjectType;
	private String borrowCd;
	private String borrowName;
	private String borrowClass;
	private String investStart;
	private String investEnd;
	private String investUserType;
	private String repayName;
	private String status;
	//2016-05-18新增属性
	private String increaseMoney;
    private String interestCoupon;
    private String increaseInterestFlag;
    public String getIncreaseMoney() {
        return increaseMoney;
    }

    public void setIncreaseMoney(String increaseMoney) {
        this.increaseMoney = increaseMoney;
    }

    public String getInterestCoupon() {
        return interestCoupon;
    }

    public void setInterestCoupon(String interestCoupon) {
        this.interestCoupon = interestCoupon;
    }

    public String getTasteMoney() {
        return tasteMoney;
    }

    public void setTasteMoney(String tasteMoney) {
        this.tasteMoney = tasteMoney;
    }

    private String tasteMoney;
	/**
	 * 检索条件 limitStart
	 */
	private int limitStart = -1;
	/**
	 * 检索条件 limitEnd
	 */
	private int limitEnd = -1;

	/**
	 * limitStart
	 * 
	 * @return the limitStart
	 */

	public int getLimitStart() {
		return limitStart;
	}

	/**
	 * @param limitStart
	 *            the limitStart to set
	 */

	public void setLimitStart(int limitStart) {
		this.limitStart = limitStart;
	}

	/**
	 * limitEnd
	 * 
	 * @return the limitEnd
	 */

	public int getLimitEnd() {
		return limitEnd;
	}

	/**
	 * @param limitEnd
	 *            the limitEnd to set
	 */

	public void setLimitEnd(int limitEnd) {
		this.limitEnd = limitEnd;
	}

	/**
	 * borrowProjectType
	 * 
	 * @return the borrowProjectType
	 */

	public String getBorrowProjectType() {
		return borrowProjectType;
	}

	/**
	 * @param borrowProjectType
	 *            the borrowProjectType to set
	 */

	public void setBorrowProjectType(String borrowProjectType) {
		this.borrowProjectType = borrowProjectType;
	}

	/**
	 * borrowCd
	 * 
	 * @return the borrowCd
	 */

	public String getBorrowCd() {
		return borrowCd;
	}

	/**
	 * @param borrowCd
	 *            the borrowCd to set
	 */

	public void setBorrowCd(String borrowCd) {
		this.borrowCd = borrowCd;
	}

	/**
	 * borrowName
	 * 
	 * @return the borrowName
	 */

	public String getBorrowName() {
		return borrowName;
	}

	/**
	 * @param borrowName
	 *            the borrowName to set
	 */

	public void setBorrowName(String borrowName) {
		this.borrowName = borrowName;
	}

	/**
	 * borrowClass
	 * 
	 * @return the borrowClass
	 */

	public String getBorrowClass() {
		return borrowClass;
	}

	/**
	 * @param borrowClass
	 *            the borrowClass to set
	 */

	public void setBorrowClass(String borrowClass) {
		this.borrowClass = borrowClass;
	}

	/**
	 * investStart
	 * 
	 * @return the investStart
	 */

	public String getInvestStart() {
		return investStart;
	}

	/**
	 * @param investStart
	 *            the investStart to set
	 */

	public void setInvestStart(String investStart) {
		this.investStart = investStart;
	}

	/**
	 * investEnd
	 * 
	 * @return the investEnd
	 */

	public String getInvestEnd() {
		return investEnd;
	}

	/**
	 * @param investEnd
	 *            the investEnd to set
	 */

	public void setInvestEnd(String investEnd) {
		this.investEnd = investEnd;
	}

	/**
	 * investUserType
	 * 
	 * @return the investUserType
	 */

	public String getInvestUserType() {
		return investUserType;
	}

	/**
	 * @param investUserType
	 *            the investUserType to set
	 */

	public void setInvestUserType(String investUserType) {
		this.investUserType = investUserType;
	}

	/**
	 * repayName
	 * 
	 * @return the repayName
	 */

	public String getRepayName() {
		return repayName;
	}

	/**
	 * @param repayName
	 *            the repayName to set
	 */

	public void setRepayName(String repayName) {
		this.repayName = repayName;
	}

	/**
	 * status
	 * 
	 * @return the status
	 */

	public String getStatus() {
		return status;
	}

	/**
	 * @param status
	 *            the status to set
	 */

	public void setStatus(String status) {
		this.status = status;
	}


	public String getIncreaseInterestFlag() {
		return increaseInterestFlag;
	}

	public void setIncreaseInterestFlag(String increaseInterestFlag) {
		this.increaseInterestFlag = increaseInterestFlag;
	}
}
