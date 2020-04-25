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
 * 还款明细
 * 
 * @author 孙亮
 * @since 2015年12月19日 上午9:29:09
 */
public class BorrowRepaymentInfoCustomize implements Serializable {
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
	private String recoverTimeStartSrch;
	/**
	 * 应还日期 检索条件
	 */
	private String recoverTimeEndSrch;
	
	/**
     * 发布日期 检索条件
     */
    private String verifyTimeStartSrch;
    /**
     * 发布日期 检索条件
     */
    private String verifyTimeEndSrch;
	/**
	 * 计划编号 检索条件 
	 */
	private String planNidSrch;
	/**
	 * 计划加入订单号 检索条件 
	 */
	private String accedeOrderIdSrch;
	/*-------------add by lsy START----------------------*/
	/**
	 * 机构名称代号 检索条件 
	 */
	private String instCodeSrch;
	/*-------------add by lsy END----------------------*/
	// ========================参数=============================
	private String assetId;//资产编号
	private String accedeOrderId;//出借订单号
	private String nid;// 出借nid
	private String borrowNid;// 借款编号
	private String planNid;//计划编号
	private String userId;// 借款人ID
	private String borrowUserName;// 借款人用户名
	private String repayOrgName;// 垫付机构用户名
	private String borrowStyle;// 类型
	private String borrowName;// 借款标题
	private String projectType;// 项目类型id
	private String projectTypeName;// 项目类型名称
	private String borrowPeriod;// 借款期限
	private String borrowApr;// 年化收益
	private String borrowAccount;// 借款金额
	private String borrowAccountYes;// 借到金额
	private String repayType;// 还款方式
	private String recoverUserId;// 出借人ID
	private String recoverTrueName;// 出借人姓名
	private String recoverUserName;// 出借人用户名
	private String recoverUserAttribute;// 出借人用户属性（当前）
	private String recoverRegionName;// 出借人所属一级分部（当前）
	private String recoverBranchName;// 出借人所属二级分部（当前）
	private String recoverDepartmentName;// 出借人所属团队（当前）
	private String referrerName;// 推荐人（当前）
	private String referrerUserId;// 推荐人ID（当前）
	private String referrerTrueName;// 推荐人姓名（当前）
	private String referrerRegionName;// 推荐人所属一级分部（当前）
	private String referrerBranchName;//推荐人所属二级分部（当前）
	private String referrerDepartmentName; //推荐人所属团队（当前）
	private String recoverPeriod;// 出借期限
	private String recoverTotal;// 出借金额
	private String recoverCapital;// 应还本金
	private String recoverInterest;// 应还利息
	private String recoverAccount;// 应还本息
	private String recoverFee;// 管理费
	private String recoverCapitalYes;// 已还本金
	private String recoverInterestYes;// 已还利息
	private String recoverAccountYes;// 已换本息
	private String recoverCapitalWait;// 未还本金
	private String recoverInterestWait;// 未还利息
	private String recoverAccountWait;// 未还本息
	private String status;// 还款状态
	private String recoverLastTime;// 最后还款日
	private String repayActionTime; //实际还款时间
	private String repayOrdid; //还款订单号
	private String repayBatchNo; //还款批次号
	private String instName; //机构名称
	//////////////////
	/**
	 * 还款日期 检索条件
	 */
	private String yesTimeStartSrch;
	/**
	 * 还款日期 检索条件
	 */
	private String yesTimeEndSrch;

	/**
	 * 列表来源标识 0：还款明细 1：批次还款-查看按钮
	 */
	private  int serchFlag = 0;
	/**
	 * 还款冻结订单号
	 */
	private String freezeOrderId;
	
	

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

    public int getSerchFlag() {
		return serchFlag;
	}

	public void setSerchFlag(int serchFlag) {
		this.serchFlag = serchFlag;
	}

	public String getYesTimeStartSrch() {
		return yesTimeStartSrch;
	}

	public void setYesTimeStartSrch(String yesTimeStartSrch) {
		this.yesTimeStartSrch = yesTimeStartSrch;
	}

	public String getYesTimeEndSrch() {
		return yesTimeEndSrch;
	}

	public void setYesTimeEndSrch(String yesTimeEndSrch) {
		this.yesTimeEndSrch = yesTimeEndSrch;
	}

	public String getAssetId() {
        return assetId;
    }

    public void setAssetId(String assetId) {
        this.assetId = assetId;
    }

    
    public String getAccedeOrderId() {
        return accedeOrderId;
    }

    public void setAccedeOrderId(String accedeOrderId) {
        this.accedeOrderId = accedeOrderId;
    }

	
	public String getRepayOrgName() {
        return repayOrgName;
    }

    public void setRepayOrgName(String repayOrgName) {
        this.repayOrgName = repayOrgName;
    }
		
	public void setLimitStart(int limitStart) {
		this.limitStart = limitStart;
	}

	public int getLimitStart() {
		return limitStart;
	}

	public int getLimitEnd() {
		return limitEnd;
	}

	public void setLimitEnd(int limitEnd) {
		this.limitEnd = limitEnd;
	}

	public String getRecoverTimeStartSrch() {
		return recoverTimeStartSrch;
	}

	public void setRecoverTimeStartSrch(String recoverTimeStartSrch) {
		this.recoverTimeStartSrch = recoverTimeStartSrch;
	}

	
	public String getRecoverTrueName() {
        return recoverTrueName;
    }

    public void setRecoverTrueName(String recoverTrueName) {
        this.recoverTrueName = recoverTrueName;
    }

    public String getRecoverTimeEndSrch() {
		return recoverTimeEndSrch;
	}

	public void setRecoverTimeEndSrch(String recoverTimeEndSrch) {
		this.recoverTimeEndSrch = recoverTimeEndSrch;
	}

	public String getNid() {
		return nid;
	}

	public void setNid(String nid) {
		this.nid = nid;
	}

	public String getBorrowNid() {
		return borrowNid;
	}

	public void setBorrowNid(String borrowNid) {
		this.borrowNid = borrowNid;
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

	public String getRecoverUserId() {
		return recoverUserId;
	}

	public void setRecoverUserId(String recoverUserId) {
		this.recoverUserId = recoverUserId;
	}

	public String getRecoverUserName() {
		return recoverUserName;
	}

	public void setRecoverUserName(String recoverUserName) {
		this.recoverUserName = recoverUserName;
	}

	public String getRecoverPeriod() {
		return recoverPeriod;
	}

	public void setRecoverPeriod(String recoverPeriod) {
		this.recoverPeriod = recoverPeriod;
	}

	public String getRecoverTotal() {
		return recoverTotal;
	}

	public void setRecoverTotal(String recoverTotal) {
		this.recoverTotal = recoverTotal;
	}

	public String getRecoverCapital() {
		return recoverCapital;
	}

	public void setRecoverCapital(String recoverCapital) {
		this.recoverCapital = recoverCapital;
	}

	public String getRecoverInterest() {
		return recoverInterest;
	}

	public void setRecoverInterest(String recoverInterest) {
		this.recoverInterest = recoverInterest;
	}

	public String getRecoverAccount() {
		return recoverAccount;
	}

	public void setRecoverAccount(String recoverAccount) {
		this.recoverAccount = recoverAccount;
	}

	public String getRecoverFee() {
		return recoverFee;
	}

	public void setRecoverFee(String recoverFee) {
		this.recoverFee = recoverFee;
	}

	public String getRecoverCapitalYes() {
		return recoverCapitalYes;
	}

	public void setRecoverCapitalYes(String recoverCapitalYes) {
		this.recoverCapitalYes = recoverCapitalYes;
	}

	public String getRecoverInterestYes() {
		return recoverInterestYes;
	}

	public void setRecoverInterestYes(String recoverInterestYes) {
		this.recoverInterestYes = recoverInterestYes;
	}

	public String getRecoverAccountYes() {
		return recoverAccountYes;
	}

	public void setRecoverAccountYes(String recoverAccountYes) {
		this.recoverAccountYes = recoverAccountYes;
	}

	public String getRecoverCapitalWait() {
		return recoverCapitalWait;
	}

	public void setRecoverCapitalWait(String recoverCapitalWait) {
		this.recoverCapitalWait = recoverCapitalWait;
	}

	public String getRecoverInterestWait() {
		return recoverInterestWait;
	}

	public void setRecoverInterestWait(String recoverInterestWait) {
		this.recoverInterestWait = recoverInterestWait;
	}

	public String getRecoverAccountWait() {
		return recoverAccountWait;
	}

	public void setRecoverAccountWait(String recoverAccountWait) {
		this.recoverAccountWait = recoverAccountWait;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getRecoverLastTime() {
		return recoverLastTime;
	}

	public void setRecoverLastTime(String recoverLastTime) {
		this.recoverLastTime = recoverLastTime;
	}

	public String getBorrowStyle() {
		return borrowStyle;
	}

	public void setBorrowStyle(String borrowStyle) {
		this.borrowStyle = borrowStyle;
	}

	public String getRecoverUserAttribute() {
		return recoverUserAttribute;
	}

	public void setRecoverUserAttribute(String recoverUserAttribute) {
		this.recoverUserAttribute = recoverUserAttribute;
	}

	public String getRecoverRegionName() {
		return recoverRegionName;
	}

	public void setRecoverRegionName(String recoverRegionName) {
		this.recoverRegionName = recoverRegionName;
	}

	public String getRecoverBranchName() {
		return recoverBranchName;
	}

	public void setRecoverBranchName(String recoverBranchName) {
		this.recoverBranchName = recoverBranchName;
	}

	public String getRecoverDepartmentName() {
		return recoverDepartmentName;
	}

	public void setRecoverDepartmentName(String recoverDepartmentName) {
		this.recoverDepartmentName = recoverDepartmentName;
	}

	public String getReferrerName() {
		return referrerName;
	}

	public void setReferrerName(String referrerName) {
		this.referrerName = referrerName;
	}

	public String getReferrerUserId() {
		return referrerUserId;
	}

	public void setReferrerUserId(String referrerUserId) {
		this.referrerUserId = referrerUserId;
	}

	public String getReferrerTrueName() {
		return referrerTrueName;
	}

	public void setReferrerTrueName(String referrerTrueName) {
		this.referrerTrueName = referrerTrueName;
	}

	public String getReferrerRegionName() {
		return referrerRegionName;
	}

	public void setReferrerRegionName(String referrerRegionName) {
		this.referrerRegionName = referrerRegionName;
	}

	public String getReferrerBranchName() {
		return referrerBranchName;
	}

	public void setReferrerBranchName(String referrerBranchName) {
		this.referrerBranchName = referrerBranchName;
	}

	public String getReferrerDepartmentName() {
		return referrerDepartmentName;
	}

	public void setReferrerDepartmentName(String referrerDepartmentName) {
		this.referrerDepartmentName = referrerDepartmentName;
	}

	public String getRepayActionTime() {
		return repayActionTime;
	}

	public void setRepayActionTime(String repayActionTime) {
		this.repayActionTime = repayActionTime;
	}

    public String getRepayOrdid() {
        return repayOrdid;
    }

    public void setRepayOrdid(String repayOrdid) {
        this.repayOrdid = repayOrdid;
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

	public String getFreezeOrderId() {
		return freezeOrderId;
	}

	public void setFreezeOrderId(String freezeOrderId) {
		this.freezeOrderId = freezeOrderId;
	}
}
