package com.hyjf.admin.manager.borrow.credittender;

import java.io.Serializable;

import com.hyjf.common.paginator.Paginator;

/**
 * @package com.hyjf.admin.maintenance.AlllBorrowCustomize;
 * @author GOGTZ-Z
 * @date 2015/07/09 17:00
 * @version V1.0  
 */
public class CreditTenderBean implements Serializable {

	/**
	 * serialVersionUID
	 */

	private static final long serialVersionUID = 387630498860089653L;

	/**
	 * 翻页机能用的隐藏变量
	 */
	private int paginatorPage = 1;

	/**
	 * 列表画面自定义标签上的用翻页对象：paginator
	 */
	private Paginator paginator;

	/**
	 * 检索条件 用户名
	 */
	private String usernameSrch;

	/**
	 * 检索条件 出让人
	 */
	private String creditUsernameSrch;
	
	/**
	 * 检索条件 债转编号
	 */
	private String creditNidSrch;

	/**
	 * 检索条件 项目编号
	 */
	private String bidNidSrch;

	/**
	 * 检索条件 订单号
	 */
	private String assignNidSrch;
	/**
	 * 检索条件 转让状态
	 */
	private String creditStatusSrch;

	/**
	 * 检索条件 发布时间开始
	 */
	private String timeStartSrch;

	/**
	 * 检索条件 发布时间开始
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
	 * 债转编号
	 */
	private String creditNid;

	/**
	 * 项目编号
	 */
	private String bidNid;
	
	/**
	 * 客户端 0pc 1ios 2Android 3微信
	 */
	private String client;

	/**
	 * creditNid
	 * 
	 * @return the creditNid
	 */

	public String getCreditNid() {
		return creditNid;
	}

	/**
	 * @param creditNid
	 *            the creditNid to set
	 */

	public void setCreditNid(String creditNid) {
		this.creditNid = creditNid;
	}

	/**
	 * bidNid
	 * 
	 * @return the bidNid
	 */

	public String getBidNid() {
		return bidNid;
	}

	/**
	 * @param bidNid
	 *            the bidNid to set
	 */

	public void setBidNid(String bidNid) {
		this.bidNid = bidNid;
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
	 * creditNidSrch
	 * 
	 * @return the creditNidSrch
	 */

	public String getCreditNidSrch() {
		return creditNidSrch;
	}

	/**
	 * @param creditNidSrch
	 *            the creditNidSrch to set
	 */

	public void setCreditNidSrch(String creditNidSrch) {
		this.creditNidSrch = creditNidSrch;
	}

	/**
	 * bidNidSrch
	 * 
	 * @return the bidNidSrch
	 */

	public String getBidNidSrch() {
		return bidNidSrch;
	}

	/**
	 * @param bidNidSrch
	 *            the bidNidSrch to set
	 */

	public void setBidNidSrch(String bidNidSrch) {
		this.bidNidSrch = bidNidSrch;
	}

	/**
	 * creditStatusSrch
	 * 
	 * @return the creditStatusSrch
	 */

	public String getCreditStatusSrch() {
		return creditStatusSrch;
	}

	/**
	 * @param creditStatusSrch
	 *            the creditStatusSrch to set
	 */

	public void setCreditStatusSrch(String creditStatusSrch) {
		this.creditStatusSrch = creditStatusSrch;
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

	public String getCreditUsernameSrch() {
		return creditUsernameSrch;
	}

	public void setCreditUsernameSrch(String creditUsernameSrch) {
		this.creditUsernameSrch = creditUsernameSrch;
	}

	public String getAssignNidSrch() {
		return assignNidSrch;
	}

	public void setAssignNidSrch(String assignNidSrch) {
		this.assignNidSrch = assignNidSrch;
	}

	public String getClient() {
		return client;
	}

	public void setClient(String client) {
		this.client = client;
	}

}
