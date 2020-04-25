/**
 * Description:（类功能描述-必填） 需要在每个方法前添加业务描述，方便业务业务行为的BI工作
 * Copyright: Copyright (HYJF Corporation)2015
 * Company: HYJF Corporation
 * @author: Administrator
 * @version: 1.0
 * Created at: 2015年11月20日 下午5:24:10
 * Modification History:
 * Modified by :
 */

package com.hyjf.mybatis.model.customize.admin.htj;

import java.io.Serializable;

/**
 * 还款列表
 *
 * @author 孙亮
 * @since 2015年12月19日 上午9:29:09
 */
public class DebtBorrowRepaymentCustomize implements Serializable {
	/**
	 * serialVersionUID:
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 检索条件 limitStart
	 */
	private int limitStart = -1;
	/**
	 * 检索条件 limitEnd
	 */
	private int limitEnd = -1;
	/**
	 * 应还日期 检索条件
	 */
	private String repayLastTimeStartSrch;
	/**
	 * 应还日期 检索条件
	 */
	private String repayLastTimeEndSrch;

	/**
     * 状态 检索条件
     */
    private String statusSrch;

	// ========================参数=============================
	private String borrowNid;// 借款编号
	private String userId;// 借款人ID
	private String borrowUserName;// 借款人用户名
	private String borrowName;// 借款标题
	private String projectType;// 项目类型id
	private String projectTypeName;// 项目类型名称
	private String borrowPeriod;// 借款期限
	private String borrowApr;// 年化收益
	private String borrowAccount;// 借款金额
	private String borrowAccountYes;// 借到金额
	private String repayType;// 还款方式中文名
	private String repayAccountCapital;// 应还本金
	private String repayAccountInterest;// 应还利息
	private String repayAccountAll;// 应还本息
	private String borrowManager;// 没用到
	private String repayFee;//管理费
	private String repayAccountCapitalYes;// 已还本金
	private String repayAccountInterestYes;// 已还利息
	private String repayAccountYes;// 已换本息
	private String repayAccountCapitalWait;// 未还本金
	private String repayAccountInterestWait;// 未还利息
	private String repayAccountWait;// 未还本息
	private String status;// 还款状态
	private String repayStatus;// 还款状态(JOB)
	private String repayLastTime;// 最后还款日
	private String repayNextTime;//下次还款日
	private String borrowStyle;//还款方式

	/**
	 * borrowStyle
	 *
	 * @return the borrowStyle
	 */

	public String getBorrowStyle() {
		return borrowStyle;
	}

	/**
	 * @param borrowStyle
	 *            the borrowStyle to set
	 */

	public void setBorrowStyle(String borrowStyle) {
		this.borrowStyle = borrowStyle;
	}

	public int getLimitStart() {
		return limitStart;
	}

	public void setLimitStart(int limitStart) {
		this.limitStart = limitStart;
	}

	public int getLimitEnd() {
		return limitEnd;
	}

	public void setLimitEnd(int limitEnd) {
		this.limitEnd = limitEnd;
	}

	public String getRepayLastTimeStartSrch() {
		return repayLastTimeStartSrch;
	}

	public void setRepayLastTimeStartSrch(String repayLastTimeStartSrch) {
		this.repayLastTimeStartSrch = repayLastTimeStartSrch;
	}

	public String getRepayLastTimeEndSrch() {
		return repayLastTimeEndSrch;
	}

	public void setRepayLastTimeEndSrch(String repayLastTimeEndSrch) {
		this.repayLastTimeEndSrch = repayLastTimeEndSrch;
	}

	public String getBorrowNid() {
		return borrowNid;
	}

	public void setBorrowNid(String borrowNid) {
		this.borrowNid = borrowNid;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getBorrowUserName() {
		return borrowUserName;
	}

	public void setBorrowUserName(String borrowUserName) {
		this.borrowUserName = borrowUserName;
	}

	public String getBorrowName() {
		return borrowName;
	}

	public void setBorrowName(String borrowName) {
		this.borrowName = borrowName;
	}

	public String getProjectType() {
		return projectType;
	}

	public void setProjectType(String projectType) {
		this.projectType = projectType;
	}

	public String getProjectTypeName() {
		return projectTypeName;
	}

	public void setProjectTypeName(String projectTypeName) {
		this.projectTypeName = projectTypeName;
	}

	public String getBorrowPeriod() {
		return borrowPeriod;
	}

	public void setBorrowPeriod(String borrowPeriod) {
		this.borrowPeriod = borrowPeriod;
	}

	public String getBorrowApr() {
		return borrowApr;
	}

	public void setBorrowApr(String borrowApr) {
		this.borrowApr = borrowApr;
	}

	public String getBorrowAccount() {
		return borrowAccount;
	}

	public void setBorrowAccount(String borrowAccount) {
		this.borrowAccount = borrowAccount;
	}

	public String getBorrowAccountYes() {
		return borrowAccountYes;
	}

	public void setBorrowAccountYes(String borrowAccountYes) {
		this.borrowAccountYes = borrowAccountYes;
	}

	public String getRepayType() {
		return repayType;
	}

	public void setRepayType(String repayType) {
		this.repayType = repayType;
	}

	public String getRepayAccountCapital() {
		return repayAccountCapital;
	}

	public void setRepayAccountCapital(String repayAccountCapital) {
		this.repayAccountCapital = repayAccountCapital;
	}

	public String getRepayAccountInterest() {
		return repayAccountInterest;
	}

	public void setRepayAccountInterest(String repayAccountInterest) {
		this.repayAccountInterest = repayAccountInterest;
	}

	public String getRepayAccountAll() {
		return repayAccountAll;
	}

	public void setRepayAccountAll(String repayAccountAll) {
		this.repayAccountAll = repayAccountAll;
	}

	public String getBorrowManager() {
		return borrowManager;
	}

	public void setBorrowManager(String borrowManager) {
		this.borrowManager = borrowManager;
	}

	public String getRepayAccountCapitalYes() {
		return repayAccountCapitalYes;
	}

	public void setRepayAccountCapitalYes(String repayAccountCapitalYes) {
		this.repayAccountCapitalYes = repayAccountCapitalYes;
	}

	public String getRepayAccountInterestYes() {
		return repayAccountInterestYes;
	}

	public void setRepayAccountInterestYes(String repayAccountInterestYes) {
		this.repayAccountInterestYes = repayAccountInterestYes;
	}

	public String getRepayAccountYes() {
		return repayAccountYes;
	}

	public void setRepayAccountYes(String repayAccountYes) {
		this.repayAccountYes = repayAccountYes;
	}

	public String getRepayAccountCapitalWait() {
		return repayAccountCapitalWait;
	}

	public void setRepayAccountCapitalWait(String repayAccountCapitalWait) {
		this.repayAccountCapitalWait = repayAccountCapitalWait;
	}

	public String getRepayAccountInterestWait() {
		return repayAccountInterestWait;
	}

	public void setRepayAccountInterestWait(String repayAccountInterestWait) {
		this.repayAccountInterestWait = repayAccountInterestWait;
	}

	public String getRepayAccountWait() {
		return repayAccountWait;
	}

	public void setRepayAccountWait(String repayAccountWait) {
		this.repayAccountWait = repayAccountWait;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getRepayLastTime() {
		return repayLastTime;
	}

	public void setRepayLastTime(String repayLastTime) {
		this.repayLastTime = repayLastTime;
	}

    public String getRepayStatus() {
        return repayStatus;
    }

    public void setRepayStatus(String repayStatus) {
        this.repayStatus = repayStatus;
    }

    public String getStatusSrch() {
        return statusSrch;
    }

    public void setStatusSrch(String statusSrch) {
        this.statusSrch = statusSrch;
    }

	public String getRepayNextTime() {
		return repayNextTime;
	}

	public void setRepayNextTime(String repayNextTime) {
		this.repayNextTime = repayNextTime;
	}

	public String getRepayFee() {
		return repayFee;
	}

	public void setRepayFee(String repayFee) {
		this.repayFee = repayFee;
	}

}
