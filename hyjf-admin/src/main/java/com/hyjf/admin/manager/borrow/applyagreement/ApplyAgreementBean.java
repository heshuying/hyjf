package com.hyjf.admin.manager.borrow.applyagreement;

import java.io.Serializable;

import com.hyjf.common.paginator.Paginator;

/**
 * 
 * 
 * 
 * @author kaka
 *
 */
public class ApplyAgreementBean implements Serializable {

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
	 * 检索条件 limitStart
	 */
	private int limitStart = -1;

	/**
	 * 检索条件 limitEnd
	 */
	private int limitEnd = -1;
	
	/**
     * 检索条件 项目编号
     */
    private String borrowNidSrch;
    
    /**
     * 检索条件 时间开始
     */
    private String timeStartSrch;

    /**
     * 检索条件 时间结束
     */
    private String timeEndSrch;
    
    /**
     * 期数
     */
    private String borrowPeriod;

	/**
	 * 申请人id
	 */
	private String applyUserId;

	/**
	 * 项目编号
	 */
	private String applyUserName;
	
	/**
	 * 协议份数
	 */
	private int agreementNumber;
	
	
	/**
     * 出借状态 0 全部；1申请中：2申请成功
     */
    private int status;
    
    /**
     * 创建时间,申请时间
     */
    private int createTime;
    
    /**
     * 更新时间
     */
    private int updateTime;

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
	
	public String getBorrowNidSrch() {
        return borrowNidSrch;
    }

    public void setBorrowNidSrch(String borrowNidSrch) {
        this.borrowNidSrch = borrowNidSrch;
    }

    public String getTimeStartSrch() {
        return timeStartSrch;
    }

    public void setTimeStartSrch(String timeStartSrch) {
        this.timeStartSrch = timeStartSrch;
    }

    public String getTimeEndSrch() {
        return timeEndSrch;
    }

    public void setTimeEndSrch(String timeEndSrch) {
        this.timeEndSrch = timeEndSrch;
    }

    public String getBorrowPeriod() {
        return borrowPeriod;
    }

    public void setBorrowPeriod(String borrowPeriod) {
        this.borrowPeriod = borrowPeriod;
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

	

	public String getApplyUserId() {
        return applyUserId;
    }

    public void setApplyUserId(String applyUserId) {
        this.applyUserId = applyUserId;
    }

    public String getApplyUserName() {
        return applyUserName;
    }

    public void setApplyUserName(String applyUserName) {
        this.applyUserName = applyUserName;
    }

    public int getAgreementNumber() {
        return agreementNumber;
    }

    public void setAgreementNumber(int agreementNumber) {
        this.agreementNumber = agreementNumber;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getCreateTime() {
        return createTime;
    }

    public void setCreateTime(int createTime) {
        this.createTime = createTime;
    }

    public int getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(int updateTime) {
        this.updateTime = updateTime;
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
