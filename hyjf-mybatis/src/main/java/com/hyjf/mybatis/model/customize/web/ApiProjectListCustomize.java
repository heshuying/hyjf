/**
 * Description:用户开户列表前端显示查询所用po
 * Copyright: Copyright (HYJF Corporation) 2015
 * Company: HYJF Corporation
 * @author: 王坤
 * @version: 1.0
 * Created at: 2015年11月11日 下午2:17:31
 * Modification History:
 * Modified by :
 */

package com.hyjf.mybatis.model.customize.web;

import java.io.Serializable;

/**
 * @author liubin
 */

public class ApiProjectListCustomize implements Serializable {

	/**
	 *序列化id
	 */
	private static final long serialVersionUID = 5748630051215873837L;
	// 项目id
	private String borrowNid;
	// 项目标题
	private String borrowName;
	// 项目还款方式
	private String borrowStyle;
	// 项目年华收益率
	private String borrowApr;
	// 项目期限
	private String borrowPeriod;
	// 项目期限类型
	private String borrowPeriodType;
	// 项目金额
	private String borrowAccount;
	// 项目进度
	private String borrowSchedule;
	// 标的状态（出借中：2、复审中：3、还款中：4、已还款：5、已流标：6）
	private String borrowStatus;
	// 剩余可投金额
	private String borrowAccountWait;
//	// 放款时间
//	private String recoverLastTime;
//	// 还款时间
//	private String repayLastTime;
	//标的Url
	private String borrowUrl;
	
	//项目类型
	private String borrowType;
	
	/**
	 * 是否产品加息
	 */
    private Integer increaseInterestFlag;
    /**
     * 加息率
     */
    private String borrowExtraYield;
    
    
	/**
	 * 构造方法
	 */

	public ApiProjectListCustomize() {
		super();
	}

	public String getBorrowNid() {
		return borrowNid;
	}

	public void setBorrowNid(String borrowNid) {
		this.borrowNid = borrowNid;
	}

	public String getBorrowName() {
		return borrowName;
	}

	public void setBorrowName(String borrowName) {
		this.borrowName = borrowName;
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

	public String getBorrowPeriod() {
		return borrowPeriod;
	}

	public void setBorrowPeriod(String borrowPeriod) {
		this.borrowPeriod = borrowPeriod;
	}

	public String getBorrowPeriodType() {
		return borrowPeriodType;
	}

	public void setBorrowPeriodType(String borrowPeriodType) {
		this.borrowPeriodType = borrowPeriodType;
	}

	public String getBorrowAccount() {
		return borrowAccount;
	}

	public void setBorrowAccount(String borrowAccount) {
		this.borrowAccount = borrowAccount;
	}

	public String getBorrowSchedule() {
		return borrowSchedule;
	}

	public void setBorrowSchedule(String borrowSchedule) {
		this.borrowSchedule = borrowSchedule;
	}

	public String getBorrowStatus() {
		return borrowStatus;
	}

	public void setBorrowStatus(String borrowStatus) {
		this.borrowStatus = borrowStatus;
	}

	public String getBorrowAccountWait() {
		return borrowAccountWait;
	}

	public void setBorrowAccountWait(String borrowAccountWait) {
		this.borrowAccountWait = borrowAccountWait;
	}

	public String getBorrowUrl() {
		return borrowUrl;
	}

	public void setBorrowUrl(String borrowUrl) {
		this.borrowUrl = borrowUrl;
	}

	public String getBorrowType() {
		return borrowType;
	}

	public void setBorrowType(String borrowType) {
		this.borrowType = borrowType;
	}

    public Integer getIncreaseInterestFlag() {
        return increaseInterestFlag;
    }

    public void setIncreaseInterestFlag(Integer increaseInterestFlag) {
        this.increaseInterestFlag = increaseInterestFlag;
    }

    public String getBorrowExtraYield() {
        return borrowExtraYield;
    }

    public void setBorrowExtraYield(String borrowExtraYield) {
        this.borrowExtraYield = borrowExtraYield;
    }
}
