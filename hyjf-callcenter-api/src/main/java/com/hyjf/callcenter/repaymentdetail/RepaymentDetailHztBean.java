/**
 * Description:江西银行账户实体类
 * Copyright: Copyright (HYJF Corporation)2015
 * Company: HYJF Corporation
 * @author: 刘彬
 * @version: 1.0
 *           Created at: 2017年07月07日 下午2:33:39
 *           Modification History:
 *           Modified by :
 */
package com.hyjf.callcenter.repaymentdetail;

import java.io.Serializable;

public class RepaymentDetailHztBean implements Serializable  {
	/**
	 * serialVersionUID:
	 */
	private static final long serialVersionUID = 2569541349432465432L;
	//用户名
    private String userName;
    //手机号
    private String mobile;
    private String borrowNid;// 借款编号
    private String borrowName;// 借款标题
    private String nid;// 出借编号
    private String repayOrdid; //还款订单号
    private String recoverCapital;// 应还本金
    private String recoverInterest;// 应还利息
    private String recoverAccount;// 应还本息
    private String recoverFee;// 管理费
    private String status;// 还款状态
    private String recoverLastTime;// 最后还款日
    private String repayActionTime; //实际还款时间
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
    public String getBorrowNid() {
        return borrowNid;
    }
    public void setBorrowNid(String borrowNid) {
        this.borrowNid = borrowNid;
    }
    public String getBorrowName() {
        return borrowName;
    }
    public void setBorrowName(String borrowName) {
        this.borrowName = borrowName;
    }
    public String getNid() {
        return nid;
    }
    public void setNid(String nid) {
        this.nid = nid;
    }
    public String getRepayOrdid() {
        return repayOrdid;
    }
    public void setRepayOrdid(String repayOrdid) {
        this.repayOrdid = repayOrdid;
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
    public String getRepayActionTime() {
        return repayActionTime;
    }
    public void setRepayActionTime(String repayActionTime) {
        this.repayActionTime = repayActionTime;
    }
    
}
