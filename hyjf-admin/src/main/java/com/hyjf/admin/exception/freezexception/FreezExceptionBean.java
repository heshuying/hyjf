package com.hyjf.admin.exception.freezexception;

import java.io.Serializable;

import com.hyjf.common.paginator.Paginator;

/**
 * @package com.hyjf.admin.maintenance.AlllBorrowCustomize;
 * @author GOGTZ-Z
 * @date 2015/07/09 17:00
 * @version V1.0  
 */
public class FreezExceptionBean implements Serializable {

	/**
	 * serialVersionUID
	 */

	private static final long serialVersionUID = 657155690866140307L;

	/**
	 * 冻结订单号
	 */
	private String trxId;
	/**
	 * 冻结订单号
	 */
	private String trxIdSrch;
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
	 * 备注
	 */
	private String notes;

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
	 * trxId
	 * 
	 * @return the trxId
	 */

	public String getTrxId() {
		return trxId;
	}

	/**
	 * @param trxId
	 *            the trxId to set
	 */

	public void setTrxId(String trxId) {
		this.trxId = trxId;
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
