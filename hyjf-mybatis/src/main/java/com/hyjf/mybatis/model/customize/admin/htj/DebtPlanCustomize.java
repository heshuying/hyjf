/**
 * Description:（类功能描述-必填） 需要在每个方法前添加业务描述，方便业务业务行为的BI工作
 * Copyright: Copyright (HYJF Corporation)2015
 * Company: HYJF Corporation
 * @author: Administrator
 * @version: 1.0
 *           Created at: 2015年11月20日 下午5:24:10
 *           Modification History:
 *           Modified by :
 */

package com.hyjf.mybatis.model.customize.admin.htj;

import java.io.Serializable;

/**
 * @author Administrator
 */

public class DebtPlanCustomize implements Serializable {

    /**
     * 此处为属性说明
     */
    private static final long serialVersionUID = 3331478970822132877L;

    public String getPlanNid() {
        return planNid;
    }

    public void setPlanNid(String planNid) {
        this.planNid = planNid;
    }

    public String getPlanName() {
        return planName;
    }

    public void setPlanName(String planName) {
        this.planName = planName;
    }

    public Integer getPlanStatus() {
        return planStatus;
    }

    public void setPlanStatus(Integer planStatus) {
        this.planStatus = planStatus;
    }

    public String getPlanTypeName() {
        return planTypeName;
    }

    public void setPlanTypeName(String planTypeName) {
        this.planTypeName = planTypeName;
    }

    public String getLockPeriod() {
        return lockPeriod;
    }

    public void setLockPeriod(String lockPeriod) {
        this.lockPeriod = lockPeriod;
    }

    public long getFullExpireTime() {
        return fullExpireTime;
    }

    public void setFullExpireTime(long fullExpireTime) {
        this.fullExpireTime = fullExpireTime;
    }

    public long getBuyEndTime() {
        return buyEndTime;
    }

    public void setBuyEndTime(long buyEndTime) {
        this.buyEndTime = buyEndTime;
    }

    public long getLiquidateShouldTime() {
        return liquidateShouldTime;
    }

    public void setLiquidateShouldTime(long liquidateShouldTime) {
        this.liquidateShouldTime = liquidateShouldTime;
    }

    public String getPlanMoney() {
        return planMoney;
    }

    public void setPlanMoney(String planMoney) {
        this.planMoney = planMoney;
    }

    public String getPlanMoneyYes() {
        return planMoneyYes;
    }

    public void setPlanMoneyYes(String planMoneyYes) {
        this.planMoneyYes = planMoneyYes;
    }

    public String getAccedeTimes() {
        return accedeTimes;
    }

    public void setAccedeTimes(String accedeTimes) {
        this.accedeTimes = accedeTimes;
    }

    public String getPlanBalance() {
        return planBalance;
    }

    public void setPlanBalance(String planBalance) {
        this.planBalance = planBalance;
    }

    public String getPlanFrost() {
        return planFrost;
    }

    public void setPlanFrost(String planFrost) {
        this.planFrost = planFrost;
    }

    public String getTenderNum() {
        return tenderNum;
    }

    public void setTenderNum(String tenderNum) {
        this.tenderNum = tenderNum;
    }

    public String getTenderCapital() {
        return tenderCapital;
    }

    public void setTenderCapital(String tenderCapital) {
        this.tenderCapital = tenderCapital;
    }

    public String getCreditNum() {
        return creditNum;
    }

    public void setCreditNum(String creditNum) {
        this.creditNum = creditNum;
    }

    public String getCreditCapital() {
        return creditCapital;
    }

    public void setCreditCapital(String creditCapital) {
        this.creditCapital = creditCapital;
    }

    public String getCreditAccount() {
        return creditAccount;
    }

    public void setCreditAccount(String creditAccount) {
        this.creditAccount = creditAccount;
    }

    public String getExpectApr() {
        return expectApr;
    }

    public void setExpectApr(String expectApr) {
        this.expectApr = expectApr;
    }

    public String getActualApr() {
        return actualApr;
    }

    public void setActualApr(String actualApr) {
        this.actualApr = actualApr;
    }

    public String getFairValueTotal() {
        return fairValueTotal;
    }

    public void setFairValueTotal(String fairValueTotal) {
        this.fairValueTotal = fairValueTotal;
    }

    private String planNid;// 计划编号

    private String planName;// 计划名称

    private String planTypeName;// 计划类型

    private Integer planStatus;// 计划状态

    private String lockPeriod;// 锁定期

    private long fullExpireTime;// 计划满标时间

    private long buyEndTime;// 计划截止时间

    private long liquidateShouldTime;// 计划应清算时间

    private String planMoney;// 计划总额

    private String planMoneyYes;// 计划加入金额

    private String accedeTimes;// 计划加入订单数

    private String planBalance;// 计划余额

    private String planFrost;// 计划冻结金额

    private String tenderNum;// 直投笔数

    private String tenderCapital;// 持有直投本金

    private String creditNum;// 持有债权笔数

    private String creditCapital;// 持有债权本金

    private String creditAccount;// 实际支付债权金额

    private String expectApr; // 预期年化收益率

    private String actualApr;// 实际年化收益率

    private String fairValueTotal;// 计划公允价值

}
