package com.hyjf.mybatis.model.customize.web;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.DecimalFormat;

import com.hyjf.common.util.CustomConstants;
/**
 * 
 * 优惠券实体
 * @author hsy
 * @version hyjf 1.0
 * @since hyjf 1.0 2016年6月12日
 * @see 下午6:36:04
 */
public class CurrentHoldRepayMentPlanListCustomize implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 358190081082338992L;
	DecimalFormat df = CustomConstants.DF_FOR_VIEW;
	/**项目编号*/
	private String borrowNid;
	/**出借编号*/
	private String nid;
	/**还款期数*/
    private String recoverPeriod;
    /**待收本息*/
    private String recoverAccountWait;
    /**待收本金*/
    private String recoverCapitalWait;
	/**待收利息*/
	private String recoverInterestWait;
	/**应还本息*/
	private String recoverAccount;
	/**待收时间*/
	private String recoverTime;
	/**还款状态*/
	private String recoveStatus;
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
    public String getRecoverPeriod() {
        return recoverPeriod;
    }
    public void setRecoverPeriod(String recoverPeriod) {
        this.recoverPeriod = recoverPeriod;
    }
    public String getRecoverAccountWait() {
        return df.format(new BigDecimal(recoverAccountWait));
    }
    public void setRecoverAccountWait(String recoverAccountWait) {
        this.recoverAccountWait = recoverAccountWait;
    }
    public String getRecoverCapitalWait() {
        return df.format(new BigDecimal(recoverCapitalWait));
    }
    public void setRecoverCapitalWait(String recoverCapitalWait) {
        this.recoverCapitalWait = recoverCapitalWait;
    }
    public String getRecoverInterestWait() {
        return df.format(new BigDecimal(recoverInterestWait));
    }
    public void setRecoverInterestWait(String recoverInterestWait) {
        this.recoverInterestWait = recoverInterestWait;
    }
    public String getRecoverAccount() {
        return df.format(new BigDecimal(recoverAccount));
    }
    public void setRecoverAccount(String recoverAccount) {
        this.recoverAccount = recoverAccount;
    }
    public String getRecoverTime() {
        return recoverTime;
    }
    public void setRecoverTime(String recoverTime) {
        this.recoverTime = recoverTime;
    }
    public String getRecoveStatus() {
        return recoveStatus;
    }
    public void setRecoveStatus(String recoveStatus) {
        this.recoveStatus = recoveStatus;
    }
	
    
}