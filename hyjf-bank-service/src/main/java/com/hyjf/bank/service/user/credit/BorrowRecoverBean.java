package com.hyjf.bank.service.user.credit;

import java.io.Serializable;

import com.hyjf.common.paginator.Paginator;

/**
 * @package com.hyjf.admin.maintenance.AlllBorrowCustomize;
 * @author GOGTZ-Z
 * @date 2015/07/09 17:00
 * @version V1.0  
 */
public class BorrowRecoverBean implements Serializable {

	/**
	 * serialVersionUID:
	 */

	private static final long serialVersionUID = 1L;

	/**
	 * 借款编号 检索条件
	 */
	private String borrowNidSrch;
	/**
	 * 借款标题 检索条件
	 */
	private String borrowNameSrch;
	/**
	 * 出借人 检索条件
	 */
	private String usernameSrch;
	/**
	 * 出借订单号 检索条件
	 */
	private String orderNumSrch;
	/**
	 * 放款状态 检索条件
	 */
	private String isRecoverSrch;
	/**
	 * 出借时间 检索条件
	 */
	private String timeRecoverStartSrch;
	/**
	 * 出借时间 检索条件
	 */
	private String timeRecoverEndSrch;
	/**
	 * 放款时间 检索条件
	 */
	private String timeStartSrch;
	/**
	 * 放款时间 检索条件
	 */
	private String timeEndSrch;
	/**
	 * 检索条件 limitStart
	 */
	private int limitStart = -1;
	/**
	 * 检索条件 limitEnd
	 */
	private int limitEnd = -1;

	/**
	 * 翻页机能用的隐藏变量
	 */
	private int paginatorPage = 1;

	/**
	 * 列表画面自定义标签上的用翻页对象：paginator
	 */
	private Paginator paginator;

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
	 * orderNumSrch
	 * 
	 * @return the orderNumSrch
	 */

	public String getOrderNumSrch() {
		return orderNumSrch;
	}

	/**
	 * @param orderNumSrch
	 *            the orderNumSrch to set
	 */

	public void setOrderNumSrch(String orderNumSrch) {
		this.orderNumSrch = orderNumSrch;
	}

	/**
	 * isRecoverSrch
	 * 
	 * @return the isRecoverSrch
	 */

	public String getIsRecoverSrch() {
		return isRecoverSrch;
	}

	/**
	 * @param isRecoverSrch
	 *            the isRecoverSrch to set
	 */

	public void setIsRecoverSrch(String isRecoverSrch) {
		this.isRecoverSrch = isRecoverSrch;
	}

	/**
	 * timeRecoverStartSrch
	 * 
	 * @return the timeRecoverStartSrch
	 */

	public String getTimeRecoverStartSrch() {
		return timeRecoverStartSrch;
	}

	/**
	 * @param timeRecoverStartSrch
	 *            the timeRecoverStartSrch to set
	 */

	public void setTimeRecoverStartSrch(String timeRecoverStartSrch) {
		this.timeRecoverStartSrch = timeRecoverStartSrch;
	}

	/**
	 * timeRecoverEndSrch
	 * 
	 * @return the timeRecoverEndSrch
	 */

	public String getTimeRecoverEndSrch() {
		return timeRecoverEndSrch;
	}

	/**
	 * @param timeRecoverEndSrch
	 *            the timeRecoverEndSrch to set
	 */

	public void setTimeRecoverEndSrch(String timeRecoverEndSrch) {
		this.timeRecoverEndSrch = timeRecoverEndSrch;
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

}
