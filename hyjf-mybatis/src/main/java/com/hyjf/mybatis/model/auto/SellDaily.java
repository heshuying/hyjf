package com.hyjf.mybatis.model.auto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class SellDaily implements Serializable {
    private Integer id;

    private String dateStr;

    private Integer drawOrder;

    private String primaryDivision;

    private String twoDivision;

    private Integer storeNum;

    private BigDecimal investTotalMonth;

    private BigDecimal repaymentTotalMonth;

    private BigDecimal investTotalPreviousMonth;

    private String investRatioGrowth;

    private BigDecimal withdrawTotalMonth;

    private String withdrawRate;

    private BigDecimal rechargeTotalMonth;

    private BigDecimal investAnnualTotalMonth;

    private BigDecimal investAnnualTotalPreviousMonth;

    private String investAnnularRatioGrowth;

    private BigDecimal investTotalYesterday;

    private BigDecimal repaymentTotalYesterday;

    private BigDecimal investAnnualTotalYesterday;

    private BigDecimal withdrawTotalYesterday;

    private BigDecimal rechargeTotalYesterday;

    private BigDecimal netCapitalInflowYesterday;

    private BigDecimal nonRepaymentToday;

    private Integer registerTotalYesterday;

    private Integer rechargeGt3000UserNum;

    private Integer investGt3000UserNum;

    private Integer investGt3000MonthUserNum;

    private Date createTime;

    private Date updateTime;

    private static final long serialVersionUID = 1L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDateStr() {
        return dateStr;
    }

    public void setDateStr(String dateStr) {
        this.dateStr = dateStr == null ? null : dateStr.trim();
    }

    public Integer getDrawOrder() {
        return drawOrder;
    }

    public void setDrawOrder(Integer drawOrder) {
        this.drawOrder = drawOrder;
    }

    public String getPrimaryDivision() {
        return primaryDivision;
    }

    public void setPrimaryDivision(String primaryDivision) {
        this.primaryDivision = primaryDivision == null ? null : primaryDivision.trim();
    }

    public String getTwoDivision() {
        return twoDivision;
    }

    public void setTwoDivision(String twoDivision) {
        this.twoDivision = twoDivision == null ? null : twoDivision.trim();
    }

    public Integer getStoreNum() {
        return storeNum;
    }

    public void setStoreNum(Integer storeNum) {
        this.storeNum = storeNum;
    }

    public BigDecimal getInvestTotalMonth() {
        return investTotalMonth;
    }

        public void setInvestTotalMonth(BigDecimal investTotalMonth) {
        this.investTotalMonth = investTotalMonth;
    }

    public BigDecimal getRepaymentTotalMonth() {
        return repaymentTotalMonth;
    }

    public void setRepaymentTotalMonth(BigDecimal repaymentTotalMonth) {
        this.repaymentTotalMonth = repaymentTotalMonth;
    }

    public BigDecimal getInvestTotalPreviousMonth() {
        return investTotalPreviousMonth;
    }

    public void setInvestTotalPreviousMonth(BigDecimal investTotalPreviousMonth) {
        this.investTotalPreviousMonth = investTotalPreviousMonth;
    }

    public String getInvestRatioGrowth() {
        return investRatioGrowth;
    }

    public void setInvestRatioGrowth(String investRatioGrowth) {
        this.investRatioGrowth = investRatioGrowth == null ? null : investRatioGrowth.trim();
    }

    public BigDecimal getWithdrawTotalMonth() {
        return withdrawTotalMonth;
    }

    public void setWithdrawTotalMonth(BigDecimal withdrawTotalMonth) {
        this.withdrawTotalMonth = withdrawTotalMonth;
    }

    public String getWithdrawRate() {
        return withdrawRate;
    }

    public void setWithdrawRate(String withdrawRate) {
        this.withdrawRate = withdrawRate == null ? null : withdrawRate.trim();
    }

    public BigDecimal getRechargeTotalMonth() {
        return rechargeTotalMonth;
    }

    public void setRechargeTotalMonth(BigDecimal rechargeTotalMonth) {
        this.rechargeTotalMonth = rechargeTotalMonth;
    }

    public BigDecimal getInvestAnnualTotalMonth() {
        return investAnnualTotalMonth;
    }

    public void setInvestAnnualTotalMonth(BigDecimal investAnnualTotalMonth) {
        this.investAnnualTotalMonth = investAnnualTotalMonth;
    }

    public BigDecimal getInvestAnnualTotalPreviousMonth() {
        return investAnnualTotalPreviousMonth;
    }

    public void setInvestAnnualTotalPreviousMonth(BigDecimal investAnnualTotalPreviousMonth) {
        this.investAnnualTotalPreviousMonth = investAnnualTotalPreviousMonth;
    }

    public String getInvestAnnularRatioGrowth() {
        return investAnnularRatioGrowth;
    }

    public void setInvestAnnularRatioGrowth(String investAnnularRatioGrowth) {
        this.investAnnularRatioGrowth = investAnnularRatioGrowth == null ? null : investAnnularRatioGrowth.trim();
    }

    public BigDecimal getInvestTotalYesterday() {
        return investTotalYesterday;
    }

    public void setInvestTotalYesterday(BigDecimal investTotalYesterday) {
        this.investTotalYesterday = investTotalYesterday;
    }

    public BigDecimal getRepaymentTotalYesterday() {
        return repaymentTotalYesterday;
    }

    public void setRepaymentTotalYesterday(BigDecimal repaymentTotalYesterday) {
        this.repaymentTotalYesterday = repaymentTotalYesterday;
    }

    public BigDecimal getInvestAnnualTotalYesterday() {
        return investAnnualTotalYesterday;
    }

    public void setInvestAnnualTotalYesterday(BigDecimal investAnnualTotalYesterday) {
        this.investAnnualTotalYesterday = investAnnualTotalYesterday;
    }

    public BigDecimal getWithdrawTotalYesterday() {
        return withdrawTotalYesterday;
    }

    public void setWithdrawTotalYesterday(BigDecimal withdrawTotalYesterday) {
        this.withdrawTotalYesterday = withdrawTotalYesterday;
    }

    public BigDecimal getRechargeTotalYesterday() {
        return rechargeTotalYesterday;
    }

    public void setRechargeTotalYesterday(BigDecimal rechargeTotalYesterday) {
        this.rechargeTotalYesterday = rechargeTotalYesterday;
    }

    public BigDecimal getNetCapitalInflowYesterday() {
        return netCapitalInflowYesterday;
    }

    public void setNetCapitalInflowYesterday(BigDecimal netCapitalInflowYesterday) {
        this.netCapitalInflowYesterday = netCapitalInflowYesterday;
    }

    public BigDecimal getNonRepaymentToday() {
        return nonRepaymentToday;
    }

    public void setNonRepaymentToday(BigDecimal nonRepaymentToday) {
        this.nonRepaymentToday = nonRepaymentToday;
    }

    public Integer getRegisterTotalYesterday() {
        return registerTotalYesterday;
    }

    public void setRegisterTotalYesterday(Integer registerTotalYesterday) {
        this.registerTotalYesterday = registerTotalYesterday;
    }

    public Integer getRechargeGt3000UserNum() {
        return rechargeGt3000UserNum;
    }

    public void setRechargeGt3000UserNum(Integer rechargeGt3000UserNum) {
        this.rechargeGt3000UserNum = rechargeGt3000UserNum;
    }

    public Integer getInvestGt3000UserNum() {
        return investGt3000UserNum;
    }

    public void setInvestGt3000UserNum(Integer investGt3000UserNum) {
        this.investGt3000UserNum = investGt3000UserNum;
    }

    public Integer getInvestGt3000MonthUserNum() {
        return investGt3000MonthUserNum;
    }

    public void setInvestGt3000MonthUserNum(Integer investGt3000MonthUserNum) {
        this.investGt3000MonthUserNum = investGt3000MonthUserNum;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public String toString() {
        return "SellDaily{" +
                "id=" + id +
                ", dateStr='" + dateStr + '\'' +
                ", drawOrder=" + drawOrder +
                ", primaryDivision='" + primaryDivision + '\'' +
                ", twoDivision='" + twoDivision + '\'' +
                ", storeNum=" + storeNum +
                ", investTotalMonth=" + investTotalMonth +
                ", repaymentTotalMonth=" + repaymentTotalMonth +
                ", investTotalPreviousMonth=" + investTotalPreviousMonth +
                ", investRatioGrowth='" + investRatioGrowth + '\'' +
                ", withdrawTotalMonth=" + withdrawTotalMonth +
                ", withdrawRate='" + withdrawRate + '\'' +
                ", rechargeTotalMonth=" + rechargeTotalMonth +
                ", investAnnualTotalMonth=" + investAnnualTotalMonth +
                ", investAnnualTotalPreviousMonth=" + investAnnualTotalPreviousMonth +
                ", investAnnularRatioGrowth='" + investAnnularRatioGrowth + '\'' +
                ", investTotalYesterday=" + investTotalYesterday +
                ", repaymentTotalYesterday=" + repaymentTotalYesterday +
                ", investAnnualTotalYesterday=" + investAnnualTotalYesterday +
                ", withdrawTotalYesterday=" + withdrawTotalYesterday +
                ", rechargeTotalYesterday=" + rechargeTotalYesterday +
                ", netCapitalInflowYesterday=" + netCapitalInflowYesterday +
                ", nonRepaymentToday=" + nonRepaymentToday +
                ", registerTotalYesterday=" + registerTotalYesterday +
                ", rechargeGt3000UserNum=" + rechargeGt3000UserNum +
                ", investGt3000UserNum=" + investGt3000UserNum +
                ", investGt3000MonthUserNum=" + investGt3000MonthUserNum +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                '}';
    }
}