package com.hyjf.mybatis.model.customize.admin.htj;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 汇添金转让类产品Bean
 * 
 * @author Administrator
 *
 */
public class DebtAdminCreditCustomize implements Serializable {

	/**
	 * @Fields serialVersionUID :
	 */
	private static final long serialVersionUID = -1880011353764677558L;

	/**
	 * 债转编号
	 */
	private String creditNid;

	/**
	 * 项目编号
	 */
	private String borrowNid;

	/**
	 * 项目类型
	 */
	private String projectType;

	/**
	 * 项目类型名称
	 */
	private String projectTypeName;

	/**
	 * 标的预期年化收益率
	 */
	private BigDecimal borrowApr;
	/**
	 * 实际年化收益
	 */
	private BigDecimal actualApr;

	/**
	 * 持有期限
	 */
	private Integer holdDays;

	/**
	 * 剩余期限
	 */
	private Integer remainDays;

	/**
	 * 还款方式
	 */
	private String borrowStyle;

	/**
	 * 转让本金
	 */
	private BigDecimal creditCapital;

	/**
	 * 待承接本金
	 */
	private BigDecimal creditCapitalWait;

	/**
	 * 垫付利息
	 */
	private BigDecimal creditInterestAdvanceWait;

	/**
	 * 债转发起类型:0:系统清算,1用户发起
	 */
	private String creditType;

	public String getCreditNid() {
		return creditNid;
	}

	public void setCreditNid(String creditNid) {
		this.creditNid = creditNid;
	}

	public String getBorrowNid() {
		return borrowNid;
	}

	public void setBorrowNid(String borrowNid) {
		this.borrowNid = borrowNid;
	}

	public BigDecimal getActualApr() {
		return actualApr;
	}

	public void setActualApr(BigDecimal actualApr) {
		this.actualApr = actualApr;
	}

	public Integer getHoldDays() {
		return holdDays;
	}

	public void setHoldDays(Integer holdDays) {
		this.holdDays = holdDays;
	}

	public Integer getRemainDays() {
		return remainDays;
	}

	public void setRemainDays(Integer remainDays) {
		this.remainDays = remainDays;
	}

	public String getBorrowStyle() {
		return borrowStyle;
	}

	public void setBorrowStyle(String borrowStyle) {
		this.borrowStyle = borrowStyle;
	}

	public BigDecimal getCreditCapital() {
		return creditCapital;
	}

	public void setCreditCapital(BigDecimal creditCapital) {
		this.creditCapital = creditCapital;
	}

	public BigDecimal getCreditCapitalWait() {
		return creditCapitalWait;
	}

	public void setCreditCapitalWait(BigDecimal creditCapitalWait) {
		this.creditCapitalWait = creditCapitalWait;
	}

	public String getCreditType() {
		return creditType;
	}

	public void setCreditType(String creditType) {
		this.creditType = creditType;
	}

	public String getProjectTypeName() {
		return projectTypeName;
	}

	public void setProjectTypeName(String projectTypeName) {
		this.projectTypeName = projectTypeName;
	}

	public BigDecimal getBorrowApr() {
		return borrowApr;
	}

	public void setBorrowApr(BigDecimal borrowApr) {
		this.borrowApr = borrowApr;
	}

	public String getProjectType() {
		return projectType;
	}

	public void setProjectType(String projectType) {
		this.projectType = projectType;
	}

	public BigDecimal getCreditInterestAdvanceWait() {
		return creditInterestAdvanceWait;
	}

	public void setCreditInterestAdvanceWait(BigDecimal creditInterestAdvanceWait) {
		this.creditInterestAdvanceWait = creditInterestAdvanceWait;
	}

}
