package com.hyjf.api.surong.borrow.status;

import java.math.BigDecimal;
import java.util.List;

import com.hyjf.base.bean.BaseResultBean;
import com.hyjf.mybatis.model.auto.BorrowRepay;
import com.hyjf.mybatis.model.auto.BorrowRepayPlan;


public class BorrowInfoSynResultBean extends BaseResultBean {
    
    /**
     * 
     */
    private static final long serialVersionUID = 3988869679360594579L;

    /**
     * 标的编号
     */
    String borrowNid;
    
    /**
     * 标的状态
     */
    String borrowStatus;
    
    /**
     * 借款金额
     */
    String account;
    
    /**
     * 借款期限
     */
    Integer borrowPeriod;
    
    /**
     * 还款总的期次
     */
    Integer repayPeriodAll;
    
    /**
     * 借贷人实际借贷到账金额（本金-服务费）
     */
    BigDecimal borrowAccountReal;
    
    /**
     * 还款计划
     */
    List<BorrowRepayPlan> repayPlanList;
    
    /**
     * 还款总信息
     */
    BorrowRepay borrowRepay;
    
    /**
     * 还款总信息
     */
    Integer borrowApicronStatus;
    
    public Integer getBorrowApicronStatus() {
        return borrowApicronStatus;
    }

    public void setBorrowApicronStatus(Integer borrowApicronStatus) {
        this.borrowApicronStatus = borrowApicronStatus;
    }

    public BigDecimal getBorrowAccountReal() {
        return borrowAccountReal;
    }

    public void setBorrowAccountReal(BigDecimal borrowAccountReal) {
        this.borrowAccountReal = borrowAccountReal;
    }

    public BigDecimal getRepayAccountWait() {
        return repayAccountWait;
    }

    public void setRepayAccountWait(BigDecimal repayAccountWait) {
        this.repayAccountWait = repayAccountWait;
    }

    public BigDecimal getRepayAccountInterestWait() {
        return repayAccountInterestWait;
    }

    public void setRepayAccountInterestWait(BigDecimal repayAccountInterestWait) {
        this.repayAccountInterestWait = repayAccountInterestWait;
    }

    public BigDecimal getRepayAccountCapitalWait() {
        return repayAccountCapitalWait;
    }

    public void setRepayAccountCapitalWait(BigDecimal repayAccountCapitalWait) {
        this.repayAccountCapitalWait = repayAccountCapitalWait;
    }

    public BigDecimal getRepayFeeWait() {
        return repayFeeWait;
    }

    public void setRepayFeeWait(BigDecimal repayFeeWait) {
        this.repayFeeWait = repayFeeWait;
    }

    public Integer getRepayPeriodAll() {
        return repayPeriodAll;
    }

    public void setRepayPeriodAll(Integer repayPeriodAll) {
        this.repayPeriodAll = repayPeriodAll;
    }

    /**
     * 标的还款方式
     */
    String borrowStyle;
    
    /**
     * 标的收益率
     */
    String borrowApr;
    
    /**
     * 服务费率
     */
    String serviceFeeRate;
    
    /**
     * 待还总额
     */
    BigDecimal repayAccountWait;
    
    /**
     * 待还利息
     */
    BigDecimal repayAccountInterestWait;
    
    /**
     * 待还本金
     */
    BigDecimal repayAccountCapitalWait;
    
    /**
     * 待还管理费
     */
    BigDecimal repayFeeWait;
    
    /**
     * 标的编号
     */
    public String getBorrowNid() {
        return borrowNid;
    }

    /**
     * 标的编号
     */
    public void setBorrowNid(String borrowNid) {
        this.borrowNid = borrowNid;
    }

    
    /**
     * 标的状态
     * @return
     */
    public String getBorrowStatus() {
        return borrowStatus;
    }

    /**
     * 标的状态
     * @return
     */
    public void setBorrowStatus(String borrowStatus) {
        this.borrowStatus = borrowStatus;
    }

    /**
     * 借款金额
     * @return
     */
    public String getAccount() {
        return account;
    }

    /**
     * 借款金额
     * 
     */
    public void setAccount(String account) {
        this.account = account;
    }

    /**
     * 借款期限
     * @return
     */
    public Integer getBorrowPeriod() {
        return borrowPeriod;
    }

    /**
     * 借款期限
     * 
     */
    public void setBorrowPeriod(Integer borrowPeriod) {
        this.borrowPeriod = borrowPeriod;
    }

    /**
     * 标的还款方式
     * @return
     */
    public String getBorrowStyle() {
        return borrowStyle;
    }

    /**
     * 标的还款方式
     * 
     */
    public void setBorrowStyle(String borrowStyle) {
        this.borrowStyle = borrowStyle;
    }

    /**
     * 标的收益率
     * @return
     */
    public String getBorrowApr() {
        return borrowApr;
    }

    /**
     * 标的收益率
     * 
     */
    public void setBorrowApr(String borrowApr) {
        this.borrowApr = borrowApr;
    }

    public String getServiceFeeRate() {
        return serviceFeeRate;
    }

    public void setServiceFeeRate(String serviceFeeRate) {
        this.serviceFeeRate = serviceFeeRate;
    }

    public List<BorrowRepayPlan> getRepayPlanList() {
        return repayPlanList;
    }

    public void setRepayPlanList(List<BorrowRepayPlan> repayPlanList) {
        this.repayPlanList = repayPlanList;
    }

    public BorrowRepay getBorrowRepay() {
        return borrowRepay;
    }

    public void setBorrowRepay(BorrowRepay borrowRepay) {
        this.borrowRepay = borrowRepay;
    }
    

}
