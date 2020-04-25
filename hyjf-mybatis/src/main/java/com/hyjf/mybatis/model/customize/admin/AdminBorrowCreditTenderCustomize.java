package com.hyjf.mybatis.model.customize.admin;

import java.io.Serializable;

@SuppressWarnings("serial")
public class AdminBorrowCreditTenderCustomize  implements Serializable {
    /* 用户名称 */
    private String userName;
    /* 债转标号 */
    private String creditNid;
    /* 出让人名称 */
    private String creditUserName;
    /* 原标标号 */
    private String bidNid;
    /* 债转投标单号 */
    private String creditTenderNid;
    /* 认购单号 */
    private String assignNid;
    /* 出借本金 */
    private String assignCapital;
    /* 债转利息 */
    private String assignInterest;
    /* 回收总额 */
    private String assignAccount;
    /* 购买价格 */
    private String assignPrice;
    /* 支付金额 */
    private String assignPay;
    /* 已还总额 */
    private String assignRepayAccount;
    /* 服务费 */
    private String creditFee;
    /* 状态 */
    private String status;
    /* 最后还款日 */
    private String assignRepayEndTime;
    /* 下次还款时间 */
    private String assignRepayNextTime;

    /* 添加时间 */
    private String addTime;
    
    /*----add by LSY START---------*/
    /* 应收本金、应收利息、应收本息、已收本息、管理费 */
    private String sumAssignCapital;
    private String sumAssignInterest;
    private String sumAssignAccount;
    private String sumAssignRepayAccount;
    private String sumCreditFee;
    /*----add by LSY END---------*/

    /*----add by LSY START---------*/
    /* 页面检索条件 */
    // 承接人ID
    private String userNameSrch;
    // 出让人ID
    private String creditUserNameSrch;
    // 债转编号
    private String creditNidSrch;
    // 还款状态
    private String statusSrch;
    // 项目编号
    private String bidNidSrch;
    // 订单号
    private String assignNidSrch;
    //下次还款时间
    private String assignRepayNextTimeStartSrch;
    //下次还款时间
    private String assignRepayNextTimeEndSrch;
    // 债权承接时间
    private String addTimeStartSrch;
    // 债权承接时间
    private String addTimeEndSrch;
	// 检索条件 limitStart
	private int limitStart = -1;
	// 检索条件 limitEnd
	private int limitEnd = -1;

    /*----add by LSY END---------*/
    
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getCreditUserName() {
        return creditUserName;
    }

    public void setCreditUserName(String creditUserName) {
        this.creditUserName = creditUserName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getBidNid() {
        return bidNid;
    }

    public void setBidNid(String bidNid) {
        this.bidNid = bidNid;
    }

    public String getCreditNid() {
        return creditNid;
    }

    public void setCreditNid(String creditNid) {
        this.creditNid = creditNid;
    }

    public String getCreditTenderNid() {
        return creditTenderNid;
    }

    public void setCreditTenderNid(String creditTenderNid) {
        this.creditTenderNid = creditTenderNid;
    }

    public String getAssignNid() {
        return assignNid;
    }

    public void setAssignNid(String assignNid) {
        this.assignNid = assignNid;
    }

    public String getAssignCapital() {
        return assignCapital;
    }

    public void setAssignCapital(String assignCapital) {
        this.assignCapital = assignCapital;
    }

    public String getAssignAccount() {
        return assignAccount;
    }

    public void setAssignAccount(String assignAccount) {
        this.assignAccount = assignAccount;
    }

    public String getAssignInterest() {
        return assignInterest;
    }

    public void setAssignInterest(String assignInterest) {
        this.assignInterest = assignInterest;
    }

    public String getAssignPrice() {
        return assignPrice;
    }

    public void setAssignPrice(String assignPrice) {
        this.assignPrice = assignPrice;
    }

    public String getAssignPay() {
        return assignPay;
    }

    public void setAssignPay(String assignPay) {
        this.assignPay = assignPay;
    }

    public String getAssignRepayAccount() {
        return assignRepayAccount;
    }

    public void setAssignRepayAccount(String assignRepayAccount) {
        this.assignRepayAccount = assignRepayAccount;
    }

    public String getAssignRepayEndTime() {
        return assignRepayEndTime;
    }

    public void setAssignRepayEndTime(String assignRepayEndTime) {
        this.assignRepayEndTime = assignRepayEndTime;
    }

    public String getAssignRepayNextTime() {
        return assignRepayNextTime;
    }

    public void setAssignRepayNextTime(String assignRepayNextTime) {
        this.assignRepayNextTime = assignRepayNextTime;
    }

    public String getCreditFee() {
        return creditFee;
    }

    public void setCreditFee(String creditFee) {
        this.creditFee = creditFee;
    }

    public String getAddTime() {
        return addTime;
    }

    public void setAddTime(String addTime) {
        this.addTime = addTime;
    }

	/**
	 * sumAssignCapital
	 * @return the sumAssignCapital
	 */
		
	public String getSumAssignCapital() {
		return sumAssignCapital;
			
	}

	/**
	 * @param sumAssignCapital the sumAssignCapital to set
	 */
		
	public void setSumAssignCapital(String sumAssignCapital) {
		this.sumAssignCapital = sumAssignCapital;
			
	}

	/**
	 * sumAssignInterest
	 * @return the sumAssignInterest
	 */
		
	public String getSumAssignInterest() {
		return sumAssignInterest;
			
	}

	/**
	 * @param sumAssignInterest the sumAssignInterest to set
	 */
		
	public void setSumAssignInterest(String sumAssignInterest) {
		this.sumAssignInterest = sumAssignInterest;
			
	}

	/**
	 * sumAssignAccount
	 * @return the sumAssignAccount
	 */
		
	public String getSumAssignAccount() {
		return sumAssignAccount;
			
	}

	/**
	 * @param sumAssignAccount the sumAssignAccount to set
	 */
		
	public void setSumAssignAccount(String sumAssignAccount) {
		this.sumAssignAccount = sumAssignAccount;
			
	}

	/**
	 * sumAssignRepayAccount
	 * @return the sumAssignRepayAccount
	 */
		
	public String getSumAssignRepayAccount() {
		return sumAssignRepayAccount;
			
	}

	/**
	 * @param sumAssignRepayAccount the sumAssignRepayAccount to set
	 */
		
	public void setSumAssignRepayAccount(String sumAssignRepayAccount) {
		this.sumAssignRepayAccount = sumAssignRepayAccount;
			
	}

	/**
	 * sumCreditFee
	 * @return the sumCreditFee
	 */
		
	public String getSumCreditFee() {
		return sumCreditFee;
			
	}

	/**
	 * @param sumCreditFee the sumCreditFee to set
	 */
		
	public void setSumCreditFee(String sumCreditFee) {
		this.sumCreditFee = sumCreditFee;
			
	}

	/**
	 * userNameSrch
	 * @return the userNameSrch
	 */
	
	public String getUserNameSrch() {
		return userNameSrch;
	}

	/**
	 * @param userNameSrch the userNameSrch to set
	 */
	
	public void setUserNameSrch(String userNameSrch) {
		this.userNameSrch = userNameSrch;
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
	 * creditNidSrch
	 * @return the creditNidSrch
	 */
	
	public String getCreditNidSrch() {
		return creditNidSrch;
	}

	/**
	 * @param creditNidSrch the creditNidSrch to set
	 */
	
	public void setCreditNidSrch(String creditNidSrch) {
		this.creditNidSrch = creditNidSrch;
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
	 * bidNidSrch
	 * @return the bidNidSrch
	 */
	
	public String getBidNidSrch() {
		return bidNidSrch;
	}

	/**
	 * @param bidNidSrch the bidNidSrch to set
	 */
	
	public void setBidNidSrch(String bidNidSrch) {
		this.bidNidSrch = bidNidSrch;
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
	 * limitStart
	 * @return the limitStart
	 */
		
	public int getLimitStart() {
		return limitStart;
			
	}

	/**
	 * @param limitStart the limitStart to set
	 */
		
	public void setLimitStart(int limitStart) {
		this.limitStart = limitStart;
			
	}

	/**
	 * limitEnd
	 * @return the limitEnd
	 */
		
	public int getLimitEnd() {
		return limitEnd;
			
	}

	/**
	 * @param limitEnd the limitEnd to set
	 */
		
	public void setLimitEnd(int limitEnd) {
		this.limitEnd = limitEnd;
			
	}

}




	