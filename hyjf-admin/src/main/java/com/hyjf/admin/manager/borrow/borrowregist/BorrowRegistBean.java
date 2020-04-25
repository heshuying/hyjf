package com.hyjf.admin.manager.borrow.borrowregist;

import com.hyjf.common.paginator.Paginator;

import java.io.Serializable;

/**
 * @package com.hyjf.admin.maintenance.AlllBorrowCustomize;
 * @author GOGTZ-Z
 * @date 2015/07/09 17:00
 * @version V1.0  
 */
public class BorrowRegistBean implements Serializable {

	/**
	 * 序列化id
	 */
	private static final long serialVersionUID = 8688089993140956227L;

	/**
	 * 翻页机能用的隐藏变量
	 */
	private int paginatorPage = 1;

	/**
	 * 列表画面自定义标签上的用翻页对象：paginator
	 */
	private Paginator paginator;
	
	/**
	 * 检索条件 项目编号
	 */
	private String borrowNid;
	
	
	/**
	 * 检索条件 项目编号
	 */
	private String borrowNidSrch;
	
	/**
	 * 检索条件 项目名称
	 */
	private String borrowNameSrch;
	
	/**
	 * 检索条件 项目名称
	 */
	private String borrowPeriodSrch;
	
	/**
	 * 检索条件 用户名
	 */
	private String usernameSrch;


	/**
	 * 检索条件 发布时间开始
	 */
	private String timeStartSrch;

	/**
	 * 检索条件 发布时间结束
	 */
	private String timeEndSrch;

	/**
	 * 标的备案状态
	 */
	private String registStatusSrch;
	
	/**
	 * 标的备案状态
	 */
	private String projectTypeSrch;
	
	/**
	 * 标的备案状态
	 */
	private String borrowStyleSrch;
	
	/**
	 * 借款人用户名
	 */
	private String userName;
	
	/**
	 * 检索条件 limitStart
	 */
	private int limitStart = -1;
	/**
	 * 检索条件 limitEnd
	 */
	private int limitEnd = -1;

    /**
     * token
     */
    private String pageToken;


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

	public int getPaginatorPage() {
		if (paginatorPage == 0) {
			paginatorPage = 1;
		}
		return paginatorPage;
	}

	public void setPaginatorPage(int paginatorPage) {
		this.paginatorPage = paginatorPage;
	}

	public Paginator getPaginator() {
		return paginator;
	}

	public void setPaginator(Paginator paginator) {
		this.paginator = paginator;
	}
	public String getRegistStatusSrch() {
		return registStatusSrch;
	}

	public void setRegistStatusSrch(String registStatusSrch) {
		this.registStatusSrch = registStatusSrch;
	}

	/**
	 * borrowNidSrch
	 * 
	 * @return the borrowNidSrch
	 */

	public String getBorrowNidSrch() {
		return borrowNidSrch;
	}

	/**
	 * @param borrowNidSrch
	 *            the borrowNidSrch to set
	 */

	public void setBorrowNidSrch(String borrowNidSrch) {
		this.borrowNidSrch = borrowNidSrch;
	}

	/**
	 * usernameSrch
	 * 
	 * @return the usernameSrch
	 */

	public String getUsernameSrch() {
		return usernameSrch;
	}

	/**
	 * @param usernameSrch
	 *            the usernameSrch to set
	 */

	public void setUsernameSrch(String usernameSrch) {
		this.usernameSrch = usernameSrch;
	}

	/**
	 * borrowNameSrch
	 * 
	 * @return the borrowNameSrch
	 */

	public String getBorrowNameSrch() {
		return borrowNameSrch;
	}

	/**
	 * @param borrowNameSrch
	 *            the borrowNameSrch to set
	 */

	public void setBorrowNameSrch(String borrowNameSrch) {
		this.borrowNameSrch = borrowNameSrch;
	}

	/**
	 * timeStartSrch
	 * 
	 * @return the timeStartSrch
	 */

	public String getTimeStartSrch() {
		return timeStartSrch;
	}

	/**
	 * @param timeStartSrch
	 *            the timeStartSrch to set
	 */

	public void setTimeStartSrch(String timeStartSrch) {
		this.timeStartSrch = timeStartSrch;
	}

	/**
	 * timeEndSrch
	 * 
	 * @return the timeEndSrch
	 */

	public String getTimeEndSrch() {
		return timeEndSrch;
	}

	/**
	 * @param timeEndSrch
	 *            the timeEndSrch to set
	 */

	public void setTimeEndSrch(String timeEndSrch) {
		this.timeEndSrch = timeEndSrch;
	}

	public String getBorrowPeriodSrch() {
		return borrowPeriodSrch;
	}

	public void setBorrowPeriodSrch(String borrowPeriodSrch) {
		this.borrowPeriodSrch = borrowPeriodSrch;
	}

	public String getProjectTypeSrch() {
		return projectTypeSrch;
	}

	public void setProjectTypeSrch(String projectTypeSrch) {
		this.projectTypeSrch = projectTypeSrch;
	}

	public String getBorrowStyleSrch() {
		return borrowStyleSrch;
	}

	public void setBorrowStyleSrch(String borrowStyleSrch) {
		this.borrowStyleSrch = borrowStyleSrch;
	}

	public String getBorrowNid() {
		return borrowNid;
	}

	public void setBorrowNid(String borrowNid) {
		this.borrowNid = borrowNid;
	}

    public String getPageToken() {
        return pageToken;
    }

    public void setPageToken(String pageToken) {
        this.pageToken = pageToken;
    }

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}
	
}
