package com.hyjf.admin.manager.hjhplan.reinvestdetail;

import java.io.Serializable;

import com.hyjf.common.paginator.Paginator;
import com.hyjf.mybatis.model.customize.web.hjh.HjhReInvestDetailCustomize;

public class HjhReInvestDetailBean extends HjhReInvestDetailCustomize implements Serializable {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 689812177508855823L;

	//时间
	private String date;
	//计划出借订单号（检索）
	private String accedeOrderIdSrch;
	//用户名（检索）
	private String userNameSrch;
	//项目编号（检索）
	private String borrowNidSrch;
	//借款期限(锁定期)（检索）
	private String lockPeriodSrch;
	//投标方式（检索）
	private String investTypeSrch;
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
	 * accedeOrderIdSrch
	 * @return the accedeOrderIdSrch
	 */
	
	public String getAccedeOrderIdSrch() {
		return accedeOrderIdSrch;
	}

	/**
	 * @param accedeOrderIdSrch the accedeOrderIdSrch to set
	 */
	
	public void setAccedeOrderIdSrch(String accedeOrderIdSrch) {
		this.accedeOrderIdSrch = accedeOrderIdSrch;
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

	/**
	 * lockPeriodSrch
	 * @return the lockPeriodSrch
	 */
	
	public String getLockPeriodSrch() {
		return lockPeriodSrch;
	}

	/**
	 * @param lockPeriodSrch the lockPeriodSrch to set
	 */
	
	public void setLockPeriodSrch(String lockPeriodSrch) {
		this.lockPeriodSrch = lockPeriodSrch;
	}

	/**
	 * investTypeSrch
	 * @return the investTypeSrch
	 */
	
	public String getInvestTypeSrch() {
		return investTypeSrch;
	}

	/**
	 * @param investTypeSrch the investTypeSrch to set
	 */
	
	public void setInvestTypeSrch(String investTypeSrch) {
		this.investTypeSrch = investTypeSrch;
	}

	/**
	 * date
	 * @return the date
	 */
	
	public String getDate() {
		return date;
	}

	/**
	 * @param date the date to set
	 */
	
	public void setDate(String date) {
		this.date = date;
	}

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
}
