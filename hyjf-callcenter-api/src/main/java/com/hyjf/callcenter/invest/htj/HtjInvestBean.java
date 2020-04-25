/**
 * Description:按照用户名/手机号查询出借明细（直投产品）返回类
 * Copyright: Copyright (HYJF Corporation)2015
 * Company: HYJF Corporation
 * @author: 刘彬
 * @version: 1.0
 *           Created at: 2017年07月07日 下午2:33:39
 *           Modification History:
 *           Modified by :
 */
package com.hyjf.callcenter.invest.htj;

import java.io.Serializable;

public class HtjInvestBean implements Serializable  {
	/**
	 * serialVersionUID:
	 */
	private static final long serialVersionUID = 256954134937625432L;
	/**
	 * 用户名
	 */
	private String userName;
	/**
	 * 手机号
	 */
	private String mobile;
	
	/**
     * 推荐人 
     */
    private String refereeUserName;
    /**
     * 计划编号
     */
    private String debtPlanNid;
    /**
     * 预期年化
     */
    private String expectApr;
    /**
     * 期限
     */
    private String debtLockPeriod;
    /**
     * 加入金额
     */
    private String accedeAccount;
    /**
     * 还款方式 
     */
    private String repaymentMethod;
    /**
     * 用户属性
     */
    private String userAttribute;
    /**
     * 平台
     */
    private String platform;
    /**
     * 加入时间 
     */
    private String createTime;
    public String getUserName() {
        return userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }
    public String getMobile() {
        return mobile;
    }
    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
    public String getRefereeUserName() {
        return refereeUserName;
    }
    public void setRefereeUserName(String refereeUserName) {
        this.refereeUserName = refereeUserName;
    }
    public String getDebtPlanNid() {
        return debtPlanNid;
    }
    public void setDebtPlanNid(String debtPlanNid) {
        this.debtPlanNid = debtPlanNid;
    }
    public String getExpectApr() {
        return expectApr;
    }
    public void setExpectApr(String expectApr) {
        this.expectApr = expectApr;
    }
    public String getDebtLockPeriod() {
        return debtLockPeriod;
    }
    public void setDebtLockPeriod(String debtLockPeriod) {
        this.debtLockPeriod = debtLockPeriod;
    }
    public String getAccedeAccount() {
        return accedeAccount;
    }
    public void setAccedeAccount(String accedeAccount) {
        this.accedeAccount = accedeAccount;
    }
    public String getRepaymentMethod() {
        return repaymentMethod;
    }
    public void setRepaymentMethod(String repaymentMethod) {
        this.repaymentMethod = repaymentMethod;
    }
    public String getUserAttribute() {
        return userAttribute;
    }
    public void setUserAttribute(String userAttribute) {
        this.userAttribute = userAttribute;
    }
    public String getPlatform() {
        return platform;
    }
    public void setPlatform(String platform) {
        this.platform = platform;
    }
    public String getCreateTime() {
        return createTime;
    }
    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
    
}
