package com.hyjf.admin.manager.borrow.borrowcreditrepay;

import java.io.Serializable;

import com.hyjf.common.paginator.Paginator;

/**
 * @package com.hyjf.admin.maintenance.AlllBorrowCustomize;
 * @author GOGTZ-Z
 * @date 2015/07/09 17:00
 * @version V1.0  
 */
public class BorrowCreditRepayBean implements Serializable {

	/**
	 * serialVersionUID
	 */

	private static final long serialVersionUID = 387630498860089653L;

	/**
	 * 翻页机能用的隐藏变量
	 */
	private int paginatorPage = 1;

	/**
	 * 子页面翻页机能用的隐藏变量
	 */
	private int infopaginatorPage = 1;
	
	/**
	 * 列表画面自定义标签上的用翻页对象：paginator
	 */
	private Paginator paginator;

	/**
	 * 列表画面自定义标签上的用翻页对象：paginator
	 */
	private Paginator infopaginator;

	/**
	 * 检索条件 用户名
	 */
	private String usernameSrch;

	/*--------add by LSY START--------------*/
	/**
	 * 检索条件 出让人
	 */
	private String creditUserNameSrch;
	/*--------add by LSY END--------------*/
	/**
	 * 检索条件 债转编号
	 */
	private String creditNidSrch;

	/*--------add by LSY START--------------*/
	/**
	 * 检索条件 还款状态
	 */
	private String statusSrch;
	/*--------add by LSY END--------------*/
	
	/**
	 * 检索条件 项目编号
	 */
	private String bidNidSrch;

	/*--------add by LSY START--------------*/
	/**
	 * 检索条件 订单号
	 */
	private String assignNidSrch;
	
	/**
	 * 检索条件 下次还款时间
	 */
    private String assignRepayNextTimeStartSrch;
    
    /**
     * 检索条件 下次还款时间
     */
    private String assignRepayNextTimeEndSrch;
    
    /**
     * 检索条件 债权承接时间
     */
    private String addTimeStartSrch;
    
    /**
     * 检索条件 债权承接时间
     */
    private String addTimeEndSrch;

	/*--------add by LSY END--------------*/
	
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
	 * 订单号
	 */
	private String assignNid;

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
	 * statusSrch
	 * @return the statusSrch
	 */
	
	public String getStatusSrch() {
		return statusSrch;
	}

	/**
	 * @param statusSrch the statusSrch to set
	 */
	
	public void setStatusSrch(String statusSrch) {
		this.statusSrch = statusSrch;
	}

	/**
	 * assignNidSrch
	 * @return the assignNidSrch
	 */
	
	public String getAssignNidSrch() {
		return assignNidSrch;
	}

	/**
	 * @param assignNidSrch the assignNidSrch to set
	 */
	
	public void setAssignNidSrch(String assignNidSrch) {
		this.assignNidSrch = assignNidSrch;
	}

	/**
	 * assignRepayNextTimeStartSrch
	 * @return the assignRepayNextTimeStartSrch
	 */
	
	public String getAssignRepayNextTimeStartSrch() {
		return assignRepayNextTimeStartSrch;
	}

	/**
	 * @param assignRepayNextTimeStartSrch the assignRepayNextTimeStartSrch to set
	 */
	
	public void setAssignRepayNextTimeStartSrch(String assignRepayNextTimeStartSrch) {
		this.assignRepayNextTimeStartSrch = assignRepayNextTimeStartSrch;
	}

	/**
	 * assignRepayNextTimeEndSrch
	 * @return the assignRepayNextTimeEndSrch
	 */
	
	public String getAssignRepayNextTimeEndSrch() {
		return assignRepayNextTimeEndSrch;
	}

	/**
	 * @param assignRepayNextTimeEndSrch the assignRepayNextTimeEndSrch to set
	 */
	
	public void setAssignRepayNextTimeEndSrch(String assignRepayNextTimeEndSrch) {
		this.assignRepayNextTimeEndSrch = assignRepayNextTimeEndSrch;
	}

	/**
	 * addTimeStartSrch
	 * @return the addTimeStartSrch
	 */
	
	public String getAddTimeStartSrch() {
		return addTimeStartSrch;
	}

	/**
	 * @param addTimeStartSrch the addTimeStartSrch to set
	 */
	
	public void setAddTimeStartSrch(String addTimeStartSrch) {
		this.addTimeStartSrch = addTimeStartSrch;
	}

	/**
	 * addTimeEndSrch
	 * @return the addTimeEndSrch
	 */
	
	public String getAddTimeEndSrch() {
		return addTimeEndSrch;
	}

	/**
	 * @param addTimeEndSrch the addTimeEndSrch to set
	 */
	
	public void setAddTimeEndSrch(String addTimeEndSrch) {
		this.addTimeEndSrch = addTimeEndSrch;
	}

	/**
	 * assignNid
	 * @return the assignNid
	 */
		
	public String getAssignNid() {
		return assignNid;
			
	}

	/**
	 * @param assignNid the assignNid to set
	 */
		
	public void setAssignNid(String assignNid) {
		this.assignNid = assignNid;
			
	}

	/**
	 * infopaginatorPage
	 * @return the infopaginatorPage
	 */
	
	public int getInfopaginatorPage() {
		return infopaginatorPage;
	}

	/**
	 * @param infopaginatorPage the infopaginatorPage to set
	 */
	
	public void setInfopaginatorPage(int infopaginatorPage) {
		this.infopaginatorPage = infopaginatorPage;
	}

	/**
	 * infopaginator
	 * @return the infopaginator
	 */
	
	public Paginator getInfopaginator() {
		return infopaginator;
	}

	/**
	 * @param infopaginator the infopaginator to set
	 */
	
	public void setInfopaginator(Paginator infopaginator) {
		this.infopaginator = infopaginator;
	}

}
