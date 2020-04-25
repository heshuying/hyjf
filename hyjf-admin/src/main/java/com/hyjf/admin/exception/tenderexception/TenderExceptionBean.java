package com.hyjf.admin.exception.tenderexception;

import java.io.Serializable;

import com.hyjf.common.paginator.Paginator;

/**
 * @package com.hyjf.admin.maintenance.AlllBorrowCustomize;
 * @author GOGTZ-Z
 * @date 2015/07/09 17:00
 * @version V1.0  
 */
public class TenderExceptionBean implements Serializable {

	/**
	 * serialVersionUID:
	 */

	private static final long serialVersionUID = 1L;

	/**
	 * 备注
	 */
	private String notes;
	/**
	 * 出借订单号
	 */
	private String nid;

	/**
	 * 检索条件 项目编号
	 */
	private String borrowNidSrch;

	/**
	 * 检索条件 出借订单号
	 */
	private String nidSrch;

	/**
	 * 检索条件 解冻订单号
	 */
	private String trxIdSrch;

	/**
	 * 检索条件 出借人
	 */
	private String tenderUserNameSrch;

	/**
	 * 检索条件 时间开始
	 */
	private String timeStartSrch;

	/**
	 * 检索条件 时间结束
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
	 * notes
	 * 
	 * @return the notes
	 */

	public String getNotes() {
		return notes;
	}

	/**
	 * @param notes
	 *            the notes to set
	 */

	public void setNotes(String notes) {
		this.notes = notes;
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
	 * nidSrch
	 * 
	 * @return the nidSrch
	 */

	public String getNidSrch() {
		return nidSrch;
	}

	/**
	 * @param nidSrch
	 *            the nidSrch to set
	 */

	public void setNidSrch(String nidSrch) {
		this.nidSrch = nidSrch;
	}

	/**
	 * trxIdSrch
	 * 
	 * @return the trxIdSrch
	 */

	public String getTrxIdSrch() {
		return trxIdSrch;
	}

	/**
	 * @param trxIdSrch
	 *            the trxIdSrch to set
	 */

	public void setTrxIdSrch(String trxIdSrch) {
		this.trxIdSrch = trxIdSrch;
	}

	/**
	 * tenderUserNameSrch
	 * 
	 * @return the tenderUserNameSrch
	 */

	public String getTenderUserNameSrch() {
		return tenderUserNameSrch;
	}

	/**
	 * @param tenderUserNameSrch
	 *            the tenderUserNameSrch to set
	 */

	public void setTenderUserNameSrch(String tenderUserNameSrch) {
		this.tenderUserNameSrch = tenderUserNameSrch;
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

}
