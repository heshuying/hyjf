package com.hyjf.admin.manager.borrow.borrowrepaymentdetails;

import java.io.Serializable;

import com.hyjf.common.paginator.Paginator;
import com.hyjf.mybatis.model.customize.BorrowRepaymentInfoCustomize;

/**
 * @package com.hyjf.admin.maintenance.AlllBorrowCustomize;
 * @author GOGTZ-Z
 * @date 2015/07/09 17:00
 * @version V1.0  
 */
public class BorrowRepaymentDetailsBean extends BorrowRepaymentInfoCustomize implements Serializable {

	/**
	 * serialVersionUID:
	 */

	private static final long serialVersionUID = 1L;

	/**
     * 应还日期 检索条件
     */
    private String recoverTimeStartSrch;
    
    /**
     * 应还日期 检索条件
     */
    private String recoverTimeEndSrch;
    /**
     * 项目编号 检索条件
     */
    private String borrowNidSrch;
    /**
     * 借款人 检索条件
     */
    private String borrowUserNameSrch;
    /**
     * 出借人 检索条件
     */
    private String recoverUserNameSrch;
    
    /**
     * 出借人 检索条件
     */
    private String recoverDepartmentNameSrch;
    
    /**
     * 还款状态 检索条件
     */
    private String statusSrch;
    /**
     * 实际回款时间 检索条件
     */
    private String recoverYesTimeStartSrch;
    
    /**
     * 实际回款时间结束 检索条件
     */
    private String recoverYesTimeEndSrch;
    
    /**
     * 推荐人 检索条件
     */
    private String inviteUserNameSrch;
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

    public String getRecoverTimeStartSrch() {
        return recoverTimeStartSrch;
    }
    public void setRecoverTimeStartSrch(String recoverTimeStartSrch) {
        this.recoverTimeStartSrch = recoverTimeStartSrch;
    }

    public String getRecoverTimeEndSrch() {
        return recoverTimeEndSrch;
    }
    public void setRecoverTimeEndSrch(String recoverTimeEndSrch) {
        this.recoverTimeEndSrch = recoverTimeEndSrch;
    }

    public String getBorrowNidSrch() {
        return borrowNidSrch;
    }

    public void setBorrowNidSrch(String borrowNidSrch) {
        this.borrowNidSrch = borrowNidSrch;
    }

    public String getBorrowUserNameSrch() {
        return borrowUserNameSrch;
    }

    public void setBorrowUserNameSrch(String borrowUserNameSrch) {
        this.borrowUserNameSrch = borrowUserNameSrch;
    }

    public String getRecoverUserNameSrch() {
        return recoverUserNameSrch;
    }

    public void setRecoverUserNameSrch(String recoverUserNameSrch) {
        this.recoverUserNameSrch = recoverUserNameSrch;
    }

    public String getStatusSrch() {
        return statusSrch;
    }

    public void setStatusSrch(String statusSrch) {
        this.statusSrch = statusSrch;
    }

    public String getRecoverYesTimeStartSrch() {
        return recoverYesTimeStartSrch;
    }

    public void setRecoverYesTimeStartSrch(String recoverYesTimeStartSrch) {
        this.recoverYesTimeStartSrch = recoverYesTimeStartSrch;
    }

    public String getRecoverYesTimeEndSrch() {
        return recoverYesTimeEndSrch;
    }

    public void setRecoverYesTimeEndSrch(String recoverYesTimeEndSrch) {
        this.recoverYesTimeEndSrch = recoverYesTimeEndSrch;
    }

    public String getInviteUserNameSrch() {
        return inviteUserNameSrch;
    }

    public void setInviteUserNameSrch(String inviteUserNameSrch) {
        this.inviteUserNameSrch = inviteUserNameSrch;
    }

    public String getRecoverDepartmentNameSrch() {
        return recoverDepartmentNameSrch;
    }

    public void setRecoverDepartmentNameSrch(String recoverDepartmentNameSrch) {
        this.recoverDepartmentNameSrch = recoverDepartmentNameSrch;
    }



}
