/**
 * Description:（类功能描述-必填） 需要在每个方法前添加业务描述，方便业务业务行为的BI工作
 * Copyright: Copyright (HYJF Corporation)2015
 * Company: HYJF Corporation
 * @author: Administrator
 * @version: 1.0
 * Created at: 2015年11月20日 下午5:24:10
 * Modification History:
 * Modified by :
 */

package com.hyjf.mybatis.model.customize;

import java.io.Serializable;

/**
 * 还款列表
 *
 * @author 孙亮
 * @since 2015年12月19日 上午9:29:09
 */
public class BorrowRepaymentCustomize implements Serializable {
	/**
	 * serialVersionUID:
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 检索条件 limitStart
	 */
	private int limitStart = -1;
	/**
	 * 检索条件 limitEnd
	 */
	private int limitEnd = -1;
	/**
	 * 应还日期 检索条件
	 */
	private String repayLastTimeStartSrch;
	/**
	 * 应还日期 检索条件
	 */
	private String repayLastTimeEndSrch;
	/**
	 * 检索条件 实际还款时间开始
	 */
	private String actulRepayTimeStartSrch;
	/**
	 * 检索条件 实际还款时间结束
	 */
	private String actulRepayTimeEndSrch;
	/**
     * 发布日期 检索条件
     */
    private String verifyTimeStartSrch;
    /**
     * 发布日期 检索条件
     */
    private String verifyTimeEndSrch;
	/**
     * 状态 检索条件
     */
    private String statusSrch;
	/**
	 * 计划编号 检索条件 
	 */
	private String planNidSrch;
	/**
	 * 计划加入订单号 检索条件 
	 */
	private String accedeOrderIdSrch;
	/*------add by LSY START-----------------------*/
	/**
	 * 资产来源 检索条件 
	 */
	private String instCodeSrch;
	/**
	 * 应还本金合计值
	 */
	private String sumRepayAccountCapital;
	/**
	 * 应还利息合计值
	 */
	private String sumRepayAccountInterest;
	/**
	 * 应还本息合计值
	 */
	private String sumRepayAccountAll;
	/**
	 * 已还本金合计值
	 */
	private String sumRepayAccountCapitalYes;
	/**
	 * 已还利息合计值
	 */
	private String sumRepayAccountInterestYes;
	/**
	 * 已还本息合计值
	 */
	private String sumRepayAccountYes;
	/*------add by LSY END-----------------------*/

	// ========================参数=============================
	private String borrowNid;// 借款编号
	private String nid;// 标识名
	private String planNid;//计划编号
	private String userId;// 借款人ID
	private String borrowUserName;// 借款人用户名
	private String borrowStyleName;// 还款方式name
	private String borrowName;// 借款标题
	private String projectType;// 项目类型id
	private String projectTypeName;// 项目类型名称
	private String borrowPeriod;// 借款期限
	private String borrowApr;// 年化收益
	private String borrowAccount;// 借款金额
	private String borrowAccountYes;// 借到金额
	private String repayType;// 还款方式中文名
	
	private String repayStyleType;// 还款方式英文名
	
	private String repayAccountCapital;// 应还本金
	private String repayAccountInterest;// 应还利息
	private String repayAccountAll;// 应还本息
	private String borrowManager;// 没用到
	private String repayFee;//管理费
	private String repayAccountCapitalYes;// 已还本金
	private String repayAccountInterestYes;// 已还利息
	private String repayAccountYes;// 已换本息
	private String repayAccountCapitalWait;// 未还本金
	private String repayAccountInterestWait;// 未还利息
	private String repayAccountWait;// 未还本息
	private String status;// 还款状态
	private String repayStatus;// 还款状态(JOB)
	private String repayLastTime;// 最后还款日
	private String repayNextTime;//下次还款日
	private String verifyTime;//审核时间（发布时间）
	private String borrowStyle;//还款方式
	private String repayMoneySource; //还款来源
	private String repayActionTime; //实际还款时间
	private String instName; //机构名称
	private String partner; // 合作机构
	
	private String borrowUserId; //借款人id
	private String createUserName;//账户操作人
	private String registUserName;//备案
	private String repayOrgUserName;//垫付机构用户名
	private boolean isMonth;//是否月标(分期标)
	
	private String countPeriod; //期次
	private String creditBorrowUserName; //承接人
	
	private String everyRepayActionTime; //每期实际还款时间

	private String freezeOrderId; //冻结订单号
	
	public String getVerifyTimeStartSrch() {
        return verifyTimeStartSrch;
    }

    public void setVerifyTimeStartSrch(String verifyTimeStartSrch) {
        this.verifyTimeStartSrch = verifyTimeStartSrch;
    }

    public String getVerifyTimeEndSrch() {
        return verifyTimeEndSrch;
    }

    public void setVerifyTimeEndSrch(String verifyTimeEndSrch) {
        this.verifyTimeEndSrch = verifyTimeEndSrch;
    }

    public String getVerifyTime() {
        return verifyTime;
    }

    public void setVerifyTime(String verifyTime) {
        this.verifyTime = verifyTime;
    }

    public boolean getIsMonth() {
        return isMonth;
    }
	
    public String getBorrowStyleName() {
        return borrowStyleName;
    }

    public void setBorrowStyleName(String borrowStyleName) {
        this.borrowStyleName = borrowStyleName;
    }

    public void setIsMonth(boolean isMonth) {
        this.isMonth = isMonth;
    }

    public String getCreateUserName() {
        return createUserName;
    }

    public void setCreateUserName(String createUserName) {
        this.createUserName = createUserName;
    }

    public String getRegistUserName() {
        return registUserName;
    }

    public void setRegistUserName(String registUserName) {
        this.registUserName = registUserName;
    }

    public String getRepayOrgUserName() {
        return repayOrgUserName;
    }

    public void setRepayOrgUserName(String repayOrgUserName) {
        this.repayOrgUserName = repayOrgUserName;
    }

    /**
	 * borrowStyle
	 *
	 * @return the borrowStyle
	 */

	public String getBorrowStyle() {
		return borrowStyle;
	}

	/**
	 * @param borrowStyle
	 *            the borrowStyle to set
	 */

	public void setBorrowStyle(String borrowStyle) {
		this.borrowStyle = borrowStyle;
	}

	public int getLimitStart() {
		return limitStart;
	}

	public void setLimitStart(int limitStart) {
		this.limitStart = limitStart;
	}

	public int getLimitEnd() {
		return limitEnd;
	}

	public void setLimitEnd(int limitEnd) {
		this.limitEnd = limitEnd;
	}

	public String getRepayLastTimeStartSrch() {
		return repayLastTimeStartSrch;
	}

	public void setRepayLastTimeStartSrch(String repayLastTimeStartSrch) {
		this.repayLastTimeStartSrch = repayLastTimeStartSrch;
	}

	public String getRepayLastTimeEndSrch() {
		return repayLastTimeEndSrch;
	}

	public void setRepayLastTimeEndSrch(String repayLastTimeEndSrch) {
		this.repayLastTimeEndSrch = repayLastTimeEndSrch;
	}

	public String getBorrowNid() {
		return borrowNid;
	}

	public void setBorrowNid(String borrowNid) {
		this.borrowNid = borrowNid;
	}
	
	public String getNid() {
        return nid;
    }

    public void setNid(String nid) {
        this.nid = nid;
    }

    public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getBorrowUserName() {
		return borrowUserName;
	}

	public void setBorrowUserName(String borrowUserName) {
		this.borrowUserName = borrowUserName;
	}

	public String getBorrowName() {
		return borrowName;
	}

	public void setBorrowName(String borrowName) {
		this.borrowName = borrowName;
	}

	public String getProjectType() {
		return projectType;
	}

	public void setProjectType(String projectType) {
		this.projectType = projectType;
	}

	public String getProjectTypeName() {
		return projectTypeName;
	}

	public void setProjectTypeName(String projectTypeName) {
		this.projectTypeName = projectTypeName;
	}

	public String getBorrowPeriod() {
		return borrowPeriod;
	}

	public void setBorrowPeriod(String borrowPeriod) {
		this.borrowPeriod = borrowPeriod;
	}

	public String getBorrowApr() {
		return borrowApr;
	}

	public void setBorrowApr(String borrowApr) {
		this.borrowApr = borrowApr;
	}

	public String getBorrowAccount() {
		return borrowAccount;
	}

	public void setBorrowAccount(String borrowAccount) {
		this.borrowAccount = borrowAccount;
	}

	public String getBorrowAccountYes() {
		return borrowAccountYes;
	}

	public void setBorrowAccountYes(String borrowAccountYes) {
		this.borrowAccountYes = borrowAccountYes;
	}

	public String getRepayType() {
		return repayType;
	}

	public void setRepayType(String repayType) {
		this.repayType = repayType;
	}

	public String getRepayAccountCapital() {
		return repayAccountCapital;
	}

	public void setRepayAccountCapital(String repayAccountCapital) {
		this.repayAccountCapital = repayAccountCapital;
	}

	public String getRepayAccountInterest() {
		return repayAccountInterest;
	}

	public void setRepayAccountInterest(String repayAccountInterest) {
		this.repayAccountInterest = repayAccountInterest;
	}

	public String getRepayAccountAll() {
		return repayAccountAll;
	}

	public void setRepayAccountAll(String repayAccountAll) {
		this.repayAccountAll = repayAccountAll;
	}

	public String getBorrowManager() {
		return borrowManager;
	}

	public void setBorrowManager(String borrowManager) {
		this.borrowManager = borrowManager;
	}

	public String getRepayAccountCapitalYes() {
		return repayAccountCapitalYes;
	}

	public void setRepayAccountCapitalYes(String repayAccountCapitalYes) {
		this.repayAccountCapitalYes = repayAccountCapitalYes;
	}

	public String getRepayAccountInterestYes() {
		return repayAccountInterestYes;
	}

	public void setRepayAccountInterestYes(String repayAccountInterestYes) {
		this.repayAccountInterestYes = repayAccountInterestYes;
	}

	public String getRepayAccountYes() {
		return repayAccountYes;
	}

	public void setRepayAccountYes(String repayAccountYes) {
		this.repayAccountYes = repayAccountYes;
	}

	public String getRepayAccountCapitalWait() {
		return repayAccountCapitalWait;
	}

	public void setRepayAccountCapitalWait(String repayAccountCapitalWait) {
		this.repayAccountCapitalWait = repayAccountCapitalWait;
	}

	public String getRepayAccountInterestWait() {
		return repayAccountInterestWait;
	}

	public void setRepayAccountInterestWait(String repayAccountInterestWait) {
		this.repayAccountInterestWait = repayAccountInterestWait;
	}

	public String getRepayAccountWait() {
		return repayAccountWait;
	}

	public void setRepayAccountWait(String repayAccountWait) {
		this.repayAccountWait = repayAccountWait;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getRepayLastTime() {
		return repayLastTime;
	}

	public void setRepayLastTime(String repayLastTime) {
		this.repayLastTime = repayLastTime;
	}

    public String getRepayStatus() {
        return repayStatus;
    }

    public void setRepayStatus(String repayStatus) {
        this.repayStatus = repayStatus;
    }

    public String getStatusSrch() {
        return statusSrch;
    }

    public void setStatusSrch(String statusSrch) {
        this.statusSrch = statusSrch;
    }

	public String getRepayNextTime() {
		return repayNextTime;
	}

	public void setRepayNextTime(String repayNextTime) {
		this.repayNextTime = repayNextTime;
	}

	public String getRepayFee() {
		return repayFee;
	}

	public void setRepayFee(String repayFee) {
		this.repayFee = repayFee;
	}

	public String getRepayMoneySource() {
		return repayMoneySource;
	}

	public void setRepayMoneySource(String repayMoneySource) {
		this.repayMoneySource = repayMoneySource;
	}

	public String getRepayActionTime() {
		return repayActionTime;
	}

	public void setRepayActionTime(String repayActionTime) {
		this.repayActionTime = repayActionTime;
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

	/**
	 * planNid
	 * @return the planNid
	 */
	
	public String getPlanNid() {
		return planNid;
	}

	/**
	 * @param planNid the planNid to set
	 */
	
	public void setPlanNid(String planNid) {
		this.planNid = planNid;
	}

	public String getAccedeOrderIdSrch() {
		return accedeOrderIdSrch;
	}

	public void setAccedeOrderIdSrch(String accedeOrderIdSrch) {
		this.accedeOrderIdSrch = accedeOrderIdSrch;
	}

	public String getInstName() {
		return instName;
	}

	public void setInstName(String instName) {
		this.instName = instName;
	}
	
	
	public String getActulRepayTimeStartSrch() {
		return actulRepayTimeStartSrch;
	}

	public void setActulRepayTimeStartSrch(String actulRepayTimeStartSrch) {
		this.actulRepayTimeStartSrch = actulRepayTimeStartSrch;
	}

	public String getActulRepayTimeEndSrch() {
		return actulRepayTimeEndSrch;
	}

	public void setActulRepayTimeEndSrch(String actulRepayTimeEndSrch) {
		this.actulRepayTimeEndSrch = actulRepayTimeEndSrch;
	}

	public String getPartner() {
		return partner;
	}

	public void setPartner(String partner) {
		this.partner = partner;
	}

	public String getRepayStyleType() {
		return repayStyleType;
	}

	public void setRepayStyleType(String repayStyleType) {
		this.repayStyleType = repayStyleType;
	}

	public String getBorrowUserId() {
		return borrowUserId;
	}

	public void setBorrowUserId(String borrowUserId) {
		this.borrowUserId = borrowUserId;
	}

	/**
	 * instCodeSrch
	 * @return the instCodeSrch
	 */
		
	public String getInstCodeSrch() {
		return instCodeSrch;
			
	}

	/**
	 * @param instCodeSrch the instCodeSrch to set
	 */
		
	public void setInstCodeSrch(String instCodeSrch) {
		this.instCodeSrch = instCodeSrch;
			
	}

	/**
	 * sumRepayAccountCapital
	 * @return the sumRepayAccountCapital
	 */
		
	public String getSumRepayAccountCapital() {
		return sumRepayAccountCapital;
			
	}

	/**
	 * @param sumRepayAccountCapital the sumRepayAccountCapital to set
	 */
		
	public void setSumRepayAccountCapital(String sumRepayAccountCapital) {
		this.sumRepayAccountCapital = sumRepayAccountCapital;
			
	}

	/**
	 * sumRepayAccountInterest
	 * @return the sumRepayAccountInterest
	 */
		
	public String getSumRepayAccountInterest() {
		return sumRepayAccountInterest;
			
	}

	/**
	 * @param sumRepayAccountInterest the sumRepayAccountInterest to set
	 */
		
	public void setSumRepayAccountInterest(String sumRepayAccountInterest) {
		this.sumRepayAccountInterest = sumRepayAccountInterest;
			
	}

	/**
	 * sumRepayAccountAll
	 * @return the sumRepayAccountAll
	 */
		
	public String getSumRepayAccountAll() {
		return sumRepayAccountAll;
			
	}

	/**
	 * @param sumRepayAccountAll the sumRepayAccountAll to set
	 */
		
	public void setSumRepayAccountAll(String sumRepayAccountAll) {
		this.sumRepayAccountAll = sumRepayAccountAll;
			
	}

	/**
	 * sumRepayAccountCapitalYes
	 * @return the sumRepayAccountCapitalYes
	 */
		
	public String getSumRepayAccountCapitalYes() {
		return sumRepayAccountCapitalYes;
			
	}

	/**
	 * @param sumRepayAccountCapitalYes the sumRepayAccountCapitalYes to set
	 */
		
	public void setSumRepayAccountCapitalYes(String sumRepayAccountCapitalYes) {
		this.sumRepayAccountCapitalYes = sumRepayAccountCapitalYes;
			
	}

	/**
	 * sumRepayAccountInterestYes
	 * @return the sumRepayAccountInterestYes
	 */
		
	public String getSumRepayAccountInterestYes() {
		return sumRepayAccountInterestYes;
			
	}

	/**
	 * @param sumRepayAccountInterestYes the sumRepayAccountInterestYes to set
	 */
		
	public void setSumRepayAccountInterestYes(String sumRepayAccountInterestYes) {
		this.sumRepayAccountInterestYes = sumRepayAccountInterestYes;
			
	}

	/**
	 * sumRepayAccountYes
	 * @return the sumRepayAccountYes
	 */
		
	public String getSumRepayAccountYes() {
		return sumRepayAccountYes;
			
	}

	/**
	 * @param sumRepayAccountYes the sumRepayAccountYes to set
	 */
		
	public void setSumRepayAccountYes(String sumRepayAccountYes) {
		this.sumRepayAccountYes = sumRepayAccountYes;
			
	}

	public String getCountPeriod() {
		return countPeriod;
	}

	public void setCountPeriod(String countPeriod) {
		this.countPeriod = countPeriod;
	}

	public String getCreditBorrowUserName() {
		return creditBorrowUserName;
	}

	public void setCreditBorrowUserName(String creditBorrowUserName) {
		this.creditBorrowUserName = creditBorrowUserName;
	}

	public String getEveryRepayActionTime() {
		return everyRepayActionTime;
	}

	public void setEveryRepayActionTime(String everyRepayActionTime) {
		this.everyRepayActionTime = everyRepayActionTime;
	}

	public boolean isMonth() {
		return isMonth;
	}

	public void setMonth(boolean month) {
		isMonth = month;
	}

	public String getFreezeOrderId() {
		return freezeOrderId;
	}

	public void setFreezeOrderId(String freezeOrderId) {
		this.freezeOrderId = freezeOrderId;
	}
}
