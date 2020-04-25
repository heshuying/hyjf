package com.hyjf.bank.service.user.credit;

import java.io.Serializable;

import com.hyjf.common.paginator.Paginator;

/**
 * @package com.hyjf.admin.maintenance.AlllBorrowCustomize;
 * @author GOGTZ-Z
 * @date 2015/07/09 17:00
 * @version V1.0  
 */
public class CreditBean implements Serializable {

	/**
	 * serialVersionUID
	 */

	private static final long serialVersionUID = 387630498860089653L;

	/**
	 * 检索条件 画面迁移标识
	 */
	private String moveFlag;

	/**
	 * 翻页机能用的隐藏变量
	 */
	private int paginatorPage = 1;

	/**
	 * 列表画面自定义标签上的用翻页对象：paginator
	 */
	private Paginator paginator;

	/**
	 * 检索条件 借款编号
	 */
	private String borrowNidSrch;
	/**
	 * 检索条件 用户名
	 */
	private String usernameSrch;

	/**
	 * 检索条件 用户名姓名
	 */
	private String truenameSrch;

	/**
	 * 检索条件 用户名电话
	 */
	private String mobileSrch;

	/**
	 * 检索条件 借款标题
	 */
	private String borrowNameSrch;

	/**
	 * 检索条件 发布时间开始
	 */
	private String timeStartSrch;

	/**
	 * 检索条件 发布时间结束
	 */
	private String timeEndSrch;
	/**
	 * 借款编码
	 */
	private String borrowNid;
	/**
	 * 借款父编号
	 */
	private String borrowPreNid;

	/**
	 * 是否交保证金
	 */
	private String isBailSrch;

	/**
	 * 检索条件 limitStart
	 */
	private int limitStart = -1;
	/**
	 * 检索条件 limitEnd
	 */
	private int limitEnd = -1;
	/**
	 * 借发标方式
	 */
	private String verifyStatus;
	/**
	 * 设定发标时间
	 */
	private String ontime;

	/**
	 * isBailSrch
	 * 
	 * @return the isBailSrch
	 */

	public String getIsBailSrch() {
		return isBailSrch;
	}

	/**
	 * @param isBailSrch
	 *            the isBailSrch to set
	 */

	public void setIsBailSrch(String isBailSrch) {
		this.isBailSrch = isBailSrch;
	}

	/**
	 * verifyStatus
	 * 
	 * @return the verifyStatus
	 */

	public String getVerifyStatus() {
		return verifyStatus;
	}

	/**
	 * @param verifyStatus
	 *            the verifyStatus to set
	 */

	public void setVerifyStatus(String verifyStatus) {
		this.verifyStatus = verifyStatus;
	}

	/**
	 * ontime
	 * 
	 * @return the ontime
	 */

	public String getOntime() {
		return ontime;
	}

	/**
	 * @param ontime
	 *            the ontime to set
	 */

	public void setOntime(String ontime) {
		this.ontime = ontime;
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
	 * moveFlag
	 * 
	 * @return the moveFlag
	 */

	public String getMoveFlag() {
		return moveFlag;
	}

	/**
	 * @param moveFlag
	 *            the moveFlag to set
	 */

	public void setMoveFlag(String moveFlag) {
		this.moveFlag = moveFlag;
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
	 * truenameSrch
	 * 
	 * @return the truenameSrch
	 */

	public String getTruenameSrch() {
		return truenameSrch;
	}

	/**
	 * @param truenameSrch
	 *            the truenameSrch to set
	 */

	public void setTruenameSrch(String truenameSrch) {
		this.truenameSrch = truenameSrch;
	}

	/**
	 * mobileSrch
	 * 
	 * @return the mobileSrch
	 */

	public String getMobileSrch() {
		return mobileSrch;
	}

	/**
	 * @param mobileSrch
	 *            the mobileSrch to set
	 */

	public void setMobileSrch(String mobileSrch) {
		this.mobileSrch = mobileSrch;
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

	/**
	 * borrowNid
	 * 
	 * @return the borrowNid
	 */

	public String getBorrowNid() {
		return borrowNid;
	}

	/**
	 * @param borrowNid
	 *            the borrowNid to set
	 */

	public void setBorrowNid(String borrowNid) {
		this.borrowNid = borrowNid;
	}

	/**
	 * serialversionuid
	 * 
	 * @return the serialversionuid
	 */

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	/**
	 * borrowPreNid
	 * 
	 * @return the borrowPreNid
	 */

	public String getBorrowPreNid() {
		return borrowPreNid;
	}

	/**
	 * @param borrowPreNid
	 *            the borrowPreNid to set
	 */

	public void setBorrowPreNid(String borrowPreNid) {
		this.borrowPreNid = borrowPreNid;
	}

}
