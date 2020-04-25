package com.hyjf.admin.manager.hjhplan.reinvestdebt;

import java.io.Serializable;

import com.hyjf.common.paginator.Paginator;
import com.hyjf.mybatis.model.customize.web.hjh.HjhReInvestDebtCustomize;

public class HjhReInvestDebtBean extends HjhReInvestDebtCustomize implements Serializable {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 689812177508855823L;

	//计划订单号（检索）
	private String planNidSrch;
	//承接计划编号（检索）
	private String assignPlanNidSrch;
	//承接订单号（检索）
	private String assignPlanOrderIdSrch;
	//承接人（检索）
	private String userNameSrch;
	//出让人（检索）
	private String creditUserNameSrch;
	//债转编号（检索）
	private String creditNidSrch;
	//原项目编号（检索）
	private String borrowNidSrch;
	//承接方式（检索）
	private String assignTypeSrch;
	//还款方式（检索）
	private String borrowStyleSrch;
    /**
     * 翻页机能用的隐藏变量
     */
    private int paginatorPage = 1;
    /**
     * 列表画面自定义标签上的用翻页对象：paginator
     */
    private Paginator paginator;
    /**
     * 检索条件 limitStart
     */
    private int limitStart = -1;
    /**
     * 检索条件 limitEnd
     */
    private int limitEnd = -1;
	
	/**
	 * paginatorPage
	 * @return the paginatorPage
	 */
	
	public int getPaginatorPage() {
        if (paginatorPage == 0) {
            paginatorPage = 1;
        }
        return paginatorPage;
	}

	/**
	 * @param paginatorPage the paginatorPage to set
	 */
	
	public void setPaginatorPage(int paginatorPage) {
		this.paginatorPage = paginatorPage;
	}

	/**
	 * paginator
	 * @return the paginator
	 */
	
	public Paginator getPaginator() {
		return paginator;
	}

	/**
	 * @param paginator the paginator to set
	 */
	
	public void setPaginator(Paginator paginator) {
		this.paginator = paginator;
	}

	/**
	 * limitStart
	 * @return the limitStart
	 */
	
	public int getLimitStart() {
		return limitStart;
	}

	/**
	 * @param limitStart the limitStart to set
	 */
	
	public void setLimitStart(int limitStart) {
		this.limitStart = limitStart;
	}

	/**
	 * limitEnd
	 * @return the limitEnd
	 */
	
	public int getLimitEnd() {
		return limitEnd;
	}

	/**
	 * @param limitEnd the limitEnd to set
	 */
	
	public void setLimitEnd(int limitEnd) {
		this.limitEnd = limitEnd;
	}

	/**
	 * planNidSrch
	 * @return the planNidSrch
	 */
	
	public String getPlanNidSrch() {
		return planNidSrch;
	}

	/**
	 * @param planNidSrch the planNidSrch to set
	 */
	
	public void setPlanNidSrch(String planNidSrch) {
		this.planNidSrch = planNidSrch;
	}

	/**
	 * assignPlanNidSrch
	 * @return the assignPlanNidSrch
	 */
	
	public String getAssignPlanNidSrch() {
		return assignPlanNidSrch;
	}

	/**
	 * @param assignPlanNidSrch the assignPlanNidSrch to set
	 */
	
	public void setAssignPlanNidSrch(String assignPlanNidSrch) {
		this.assignPlanNidSrch = assignPlanNidSrch;
	}

	/**
	 * assignPlanOrderIdSrch
	 * @return the assignPlanOrderIdSrch
	 */
	
	public String getAssignPlanOrderIdSrch() {
		return assignPlanOrderIdSrch;
	}

	/**
	 * @param assignPlanOrderIdSrch the assignPlanOrderIdSrch to set
	 */
	
	public void setAssignPlanOrderIdSrch(String assignPlanOrderIdSrch) {
		this.assignPlanOrderIdSrch = assignPlanOrderIdSrch;
	}

	/**
	 * userNameSrch
	 * @return the userNameSrch
	 */
	
	public String getUserNameSrch() {
		return userNameSrch;
	}

	/**
	 * @param userNameSrch the userNameSrch to set
	 */
	
	public void setUserNameSrch(String userNameSrch) {
		this.userNameSrch = userNameSrch;
	}

	/**
	 * creditUserNameSrch
	 * @return the creditUserNameSrch
	 */
	
	public String getCreditUserNameSrch() {
		return creditUserNameSrch;
	}

	/**
	 * @param creditUserNameSrch the creditUserNameSrch to set
	 */
	
	public void setCreditUserNameSrch(String creditUserNameSrch) {
		this.creditUserNameSrch = creditUserNameSrch;
	}

	/**
	 * creditNidSrch
	 * @return the creditNidSrch
	 */
	
	public String getCreditNidSrch() {
		return creditNidSrch;
	}

	/**
	 * @param creditNidSrch the creditNidSrch to set
	 */
	
	public void setCreditNidSrch(String creditNidSrch) {
		this.creditNidSrch = creditNidSrch;
	}

	/**
	 * borrowNidSrch
	 * @return the borrowNidSrch
	 */
	
	public String getBorrowNidSrch() {
		return borrowNidSrch;
	}

	/**
	 * @param borrowNidSrch the borrowNidSrch to set
	 */
	
	public void setBorrowNidSrch(String borrowNidSrch) {
		this.borrowNidSrch = borrowNidSrch;
	}

	/**
	 * assignTypeSrch
	 * @return the assignTypeSrch
	 */
	
	public String getAssignTypeSrch() {
		return assignTypeSrch;
	}

	/**
	 * @param assignTypeSrch the assignTypeSrch to set
	 */
	
	public void setAssignTypeSrch(String assignTypeSrch) {
		this.assignTypeSrch = assignTypeSrch;
	}

	/**
	 * borrowStyleSrch
	 * @return the borrowStyleSrch
	 */
	
	public String getBorrowStyleSrch() {
		return borrowStyleSrch;
	}

	/**
	 * @param borrowStyleSrch the borrowStyleSrch to set
	 */
	
	public void setBorrowStyleSrch(String borrowStyleSrch) {
		this.borrowStyleSrch = borrowStyleSrch;
	}
}
