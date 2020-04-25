package com.hyjf.mybatis.model.customize.admin;

import java.io.Serializable;
import java.math.BigDecimal;

import com.hyjf.common.paginator.Paginator;

/**
 * 
 * 
 * 
 * @author zhadaojian
 *
 */
public class BorrowRepayAgreementCustomize implements Serializable {

	/**
	 * serialVersionUID
	 */

	private static final long serialVersionUID = 387630498860089653L;

	 private String borrowNidSrch;
	    /**
	     * 检索条件 时间开始
	     */
	    private String timeStartSrch;

	    /**
	     * 检索条件 时间结束
	     */
	    private String timeEndSrch;
	    
	    
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
	 * 实际还款人(垫付机构)id
	 */
	private String repayUserId;

	/**
	 * 实际还款人(垫付机构)
	 */
	private String repayUsername;
	
	/**
	 * 借款编号
	 */
	private String borrowNid;
	private String nid;
	/**
	 * 还款状态
	 */
	private String repayStatus;
	
	
	/**
     * 是否是分期
     */
    private boolean isMonth;
    /**
     * 是否申请了垫付协议
     */
    private int applyagreements;
    
    
    public String getRepayStatus() {
        return repayStatus;
    }

    public void setRepayStatus(String repayStatus) {
        this.repayStatus = repayStatus;
    }

    public int getApplyagreements() {
        return applyagreements;
    }

    public void setApplyagreements(int applyagreements) {
        this.applyagreements = applyagreements;
    }

    public void setMonth(boolean isMonth) {
        this.isMonth = isMonth;
    }

    public boolean getIsMonth() {
        return isMonth;
    }
	
	public String getNid() {
        return nid;
    }

    public void setNid(String nid) {
        this.nid = nid;
    }
    
    

    /**
     * 资产来源
     */
    private String borrowProjectSource;
    
    /**
     * 期数
     */
    private int repayPeriod;
    
    /**
     * 垫付时间
     */
    private String repayYseTime;
    
    /**
     * 垫付总额
     */
    private BigDecimal repayCapital;
    
    /**
     * 借款人ID
     */
    private int userId;
    /**
     * 借款人
     */
    private String userName;

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

	
    public String getRepayUserId() {
        return repayUserId;
    }

    public void setRepayUserId(String repayUserId) {
        this.repayUserId = repayUserId;
    }

    public String getRepayUsername() {
        return repayUsername;
    }

    public void setRepayUsername(String repayUsername) {
        this.repayUsername = repayUsername;
    }

    public String getBorrowNid() {
        return borrowNid;
    }

    public void setBorrowNid(String borrowNid) {
        this.borrowNid = borrowNid;
    }

    public String getBorrowProjectSource() {
        return borrowProjectSource;
    }

    public void setBorrowProjectSource(String borrowProjectSource) {
        this.borrowProjectSource = borrowProjectSource;
    }

    public int getRepayPeriod() {
        return repayPeriod;
    }

    public void setRepayPeriod(int repayPeriod) {
        this.repayPeriod = repayPeriod;
    }

    public String getRepayYseTime() {
        return repayYseTime;
    }

    public void setRepayYseTime(String repayYseTime) {
        this.repayYseTime = repayYseTime;
    }

    public BigDecimal getRepayCapital() {
        return repayCapital;
    }

    public void setRepayCapital(BigDecimal repayCapital) {
        this.repayCapital = repayCapital;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
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
