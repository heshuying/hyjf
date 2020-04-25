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
public class CurrentHoldRepayMentPlanDetailsCustomize implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 358190081082338992L;
	DecimalFormat df = CustomConstants.DF_FOR_VIEW;
	/**待收本息总额*/
	private String recoverAccountWait;
	/**待收本金总额*/
	private String recoverCapitalWait;
	/**待收利息总额*/
    private String recoverInterestWait;
    /**已收本息总额*/
    private String recoverAccountYes;
    /**出借时间*/
    private String addtime;
	/**项目编号*/
	private String borrowNid;
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
    public String getRecoverAccountYes() {
        return df.format(new BigDecimal(recoverAccountYes));
    }
    public void setRecoverAccountYes(String recoverAccountYes) {
        this.recoverAccountYes = recoverAccountYes;
    }
    public String getAddtime() {
        return addtime;
    }
    public void setAddtime(String addtime) {
        this.addtime = addtime;
    }
    public String getBorrowNid() {
        return borrowNid;
    }
    public void setBorrowNid(String borrowNid) {
        this.borrowNid = borrowNid;
    }
	
}