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
public class BorrowRepaymentPlanCustomize implements Serializable {
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
     * 发布日期 检索条件
     */
    private String verifyTimeStartSrch;
    /**
     * 发布日期 检索条件
     */
    private String verifyTimeEndSrch;
	
	/**
     * 机构名称代号 检索条件 
     */
    private String instCodeSrch;

	/**
	 * 实际还款时间 开始 检索条件
	 */
	private String actulRepayTimeStartSrch;

	/**
	 * 实际还款时间 结束 检索条件
	 */
	private String  actulRepayTimeEndSrch;

	private String planNidSrch;

	private String repayStyleType;

	// ========================参数=============================
	private String nid;// 出借nid
	private String borrowNid;// 借款编号
	private String userId;// 借款人ID
	private String borrowUserName;// 借款人用户名
	private String borrowName;// 借款标题
	private String projectType;// 项目类型id
	private String projectTypeName;// 项目类型名称
	private String borrowPeriod;// 借款期限
	private String borrowApr;// 年化收益
	private String borrowAccount;// 借款金额
	private String borrowAccountYes;// 借到金额
	private String repayType;// 还款方式
	private String repayPeriod;// 还款期次
	private String repayCapital;// 应还本金
	private String repayInterest;// 应还利息
	private String repayAccount;// 应还本息
	private String repayFee;// 应收管理费
	private String tiqiantianshu;// 提前天数
	private String shaohuanlixi;// 少还利息
	private String yanqitianshu;// 延期天数
	private String yanqilixi;// 延期利息
	private String yuqitianshu;// 逾期天数
	private String yuqilixi;// 逾期利息
	private String yinghuanzonge;// 应还总额
	private String shihuanzonge;// 实还总额
	private String status;// 还款状态
	private String repayActionTime;// 实际还款日期
	private String verifyTime;// 发布时间
	private String repayLastTime;// 应还日期
	private String repayMoneySource; //还款来源
	private String accedeOrderId;//汇计划加入订单号
	private String isMonth;//是否分期
	private String instName; //机构名称
	private String repayAccountCapitalWait;// 未还本金
	private String repayAccountInterestWait;// 未还利息
	
	
	public String getVerifyTime() {
        return verifyTime;
    }

    public void setVerifyTime(String verifyTime) {
        this.verifyTime = verifyTime;
    }

    public String getInstCodeSrch() {
        return instCodeSrch;
    }

    public void setInstCodeSrch(String instCodeSrch) {
        this.instCodeSrch = instCodeSrch;
    }

    public String getInstName() {
        return instName;
    }

    public void setInstName(String instName) {
        this.instName = instName;
    }

    public String getIsMonth() {
        return isMonth;
    }

    public void setIsMonth(String isMonth) {
        this.isMonth = isMonth;
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

	public String getRepayPeriod() {
		return repayPeriod;
	}

	public void setRepayPeriod(String repayPeriod) {
		this.repayPeriod = repayPeriod;
	}

	public String getRepayCapital() {
		return repayCapital;
	}

	public void setRepayCapital(String repayCapital) {
		this.repayCapital = repayCapital;
	}

	public String getRepayInterest() {
		return repayInterest;
	}

	public void setRepayInterest(String repayInterest) {
		this.repayInterest = repayInterest;
	}

	public String getRepayAccount() {
		return repayAccount;
	}

	public void setRepayAccount(String repayAccount) {
		this.repayAccount = repayAccount;
	}

	public String getRepayFee() {
		return repayFee;
	}

	public void setRepayFee(String repayFee) {
		this.repayFee = repayFee;
	}

	public String getTiqiantianshu() {
		return tiqiantianshu;
	}

	public void setTiqiantianshu(String tiqiantianshu) {
		this.tiqiantianshu = tiqiantianshu;
	}

	public String getShaohuanlixi() {
		return shaohuanlixi;
	}

	public void setShaohuanlixi(String shaohuanlixi) {
		this.shaohuanlixi = shaohuanlixi;
	}

	public String getYanqitianshu() {
		return yanqitianshu;
	}

	public void setYanqitianshu(String yanqitianshu) {
		this.yanqitianshu = yanqitianshu;
	}

	public String getYanqilixi() {
		return yanqilixi;
	}

	public void setYanqilixi(String yanqilixi) {
		this.yanqilixi = yanqilixi;
	}

	public String getYuqitianshu() {
		return yuqitianshu;
	}

	public void setYuqitianshu(String yuqitianshu) {
		this.yuqitianshu = yuqitianshu;
	}

	public String getYuqilixi() {
		return yuqilixi;
	}

	public void setYuqilixi(String yuqilixi) {
		this.yuqilixi = yuqilixi;
	}

	public String getYinghuanzonge() {
		return yinghuanzonge;
	}

	public void setYinghuanzonge(String yinghuanzonge) {
		this.yinghuanzonge = yinghuanzonge;
	}

	public String getShihuanzonge() {
		return shihuanzonge;
	}

	public void setShihuanzonge(String shihuanzonge) {
		this.shihuanzonge = shihuanzonge;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getRepayActionTime() {
		return repayActionTime;
	}

	public void setRepayActionTime(String repayActionTime) {
		this.repayActionTime = repayActionTime;
	}

	public String getRepayLastTime() {
		return repayLastTime;
	}

	public void setRepayLastTime(String repayLastTime) {
		this.repayLastTime = repayLastTime;
	}

	public String getRepayMoneySource() {
		return repayMoneySource;
	}

	public void setRepayMoneySource(String repayMoneySource) {
		this.repayMoneySource = repayMoneySource;
	}

	public String getAccedeOrderId() {
		return accedeOrderId;
	}

	public void setAccedeOrderId(String accedeOrderId) {
		this.accedeOrderId = accedeOrderId;
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

	public String getPlanNidSrch() {
		return planNidSrch;
	}

	public void setPlanNidSrch(String planNidSrch) {
		this.planNidSrch = planNidSrch;
	}

	public String getRepayStyleType() {
		return repayStyleType;
	}

	public void setRepayStyleType(String repayStyleType) {
		this.repayStyleType = repayStyleType;
	}
}
