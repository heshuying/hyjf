package com.hyjf.admin.manager.borrow.borrowrepaymentinfo;

import com.hyjf.common.paginator.Paginator;
import com.hyjf.mybatis.model.customize.BorrowRepaymentInfoCustomize;

import java.io.Serializable;

/**
 * @package com.hyjf.admin.maintenance.AlllBorrowCustomize;
 * @author GOGTZ-Z
 * @date 2015/07/09 17:00
 * @version V1.0  
 */
public class BorrowRepaymentInfoBean extends BorrowRepaymentInfoCustomize implements Serializable {

	/**
	 * serialVersionUID:
	 */

	private static final long serialVersionUID = 1L;

	/**
	 * 项目编号 检索条件
	 */
	private String borrowNidSrch;
	/**
	 * 项目编号 检索条件
	 */
	private String planNidSrch;
	/**
	 * 项目名称 检索条件
	 */
	private String borrowNameSrch;
	/**
	 * 用户名 检索条件
	 */
	private String usernameSrch;
	/**
	 * 推荐人 检索条件
	 */
	private String referrerNameSrch;
	/**
	 * 还款方式 检索条件
	 */
	private String borrowStyleSrch;
	/**
	 * 操作平台 检索条件
	 */
	private String clientSrch;
	/**
	 * 渠道 检索条件
	 */
	private String utmIdSrch;
	
	/**
     * 还款批次号 检索条件
     */
    private String repayBatchNo;
	/**
	 * 出借时间 检索条件
	 */
	private String timeStartSrch;
	/**
	 * 出借时间 检索条件
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
	/**
	 * 列表来源标识 0：还款明细 1：批次还款-查看按钮
	 */
	private  int serchFlag = 0;

	public int getSerchFlag() {
		return serchFlag;
	}

	public void setSerchFlag(int serchFlag) {
		this.serchFlag = serchFlag;
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
	 * referrerNameSrch
	 * 
	 * @return the referrerNameSrch
	 */

	public String getReferrerNameSrch() {
		return referrerNameSrch;
	}

	/**
	 * @param referrerNameSrch
	 *            the referrerNameSrch to set
	 */

	public void setReferrerNameSrch(String referrerNameSrch) {
		this.referrerNameSrch = referrerNameSrch;
	}

	/**
	 * borrowStyleSrch
	 * 
	 * @return the borrowStyleSrch
	 */

	public String getBorrowStyleSrch() {
		return borrowStyleSrch;
	}

	/**
	 * @param borrowStyleSrch
	 *            the borrowStyleSrch to set
	 */

	public void setBorrowStyleSrch(String borrowStyleSrch) {
		this.borrowStyleSrch = borrowStyleSrch;
	}

	/**
	 * clientSrch
	 * 
	 * @return the clientSrch
	 */

	public String getClientSrch() {
		return clientSrch;
	}

	/**
	 * @param clientSrch
	 *            the clientSrch to set
	 */

	public void setClientSrch(String clientSrch) {
		this.clientSrch = clientSrch;
	}

	/**
	 * utmIdSrch
	 * 
	 * @return the utmIdSrch
	 */

	public String getUtmIdSrch() {
		return utmIdSrch;
	}

	/**
	 * @param utmIdSrch
	 *            the utmIdSrch to set
	 */

	public void setUtmIdSrch(String utmIdSrch) {
		this.utmIdSrch = utmIdSrch;
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

    public String getRepayBatchNo() {
        return repayBatchNo;
    }

    public void setRepayBatchNo(String repayBatchNo) {
        this.repayBatchNo = repayBatchNo;
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

}
