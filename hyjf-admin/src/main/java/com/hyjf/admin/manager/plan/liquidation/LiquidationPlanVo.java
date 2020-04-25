/*
 * Copyright(c) 2012-2016 JD Pharma.Ltd. All Rights Reserved.
 * 
 * _ooOoo_
 * o8888888o
 * 88" . "88
 * (| -_- |)
 * O\ = /O
 * ____/`---'\____
 * .' \\| |// `.
 * / \\||| : |||// \
 * / _||||| -:- |||||- \
 * | | \\\ - /// | |
 * | \_| ''\---/'' | |
 * \ .-\__ `-` ___/-. /
 * ___`. .' /--.--\ `. . __
 * ."" '< `.___\_<|>_/___.' >'"".
 * | | : `- \`.;`\ _ /`;.`/ - ` : | |
 * \ \ `-. \_ __\ /__ _/ .-` / /
 * ======`-.____`-.___\_____/___.-`____.-'======
 * `=---='
 * ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
 * 佛祖保佑 永无BUG
 */

package com.hyjf.admin.manager.plan.liquidation;

import java.io.Serializable;

/**
 * 清算计划展示类
 * @author renxingchen
 * @version hyjf 1.0
 * @since hyjf 1.0 2016年11月21日
 * @see 下午4:10:33
 */
public class LiquidationPlanVo implements Serializable {

    /**
     * 此处为属性说明
     */
    private static final long serialVersionUID = 6685065026858963137L;

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

    public String getFullExpireTime() {
        return fullExpireTime;
    }

    public void setFullExpireTime(String fullExpireTime) {
        this.fullExpireTime = fullExpireTime;
    }

    public String getBuyEndTime() {
        return buyEndTime;
    }

    public void setBuyEndTime(String buyEndTime) {
        this.buyEndTime = buyEndTime;
    }

    public String getLiquidateShouldTime() {
        return liquidateShouldTime;
    }

    public void setLiquidateShouldTime(String liquidateShouldTime) {
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

    private String planNid;// 计划编号

    private String planName;// 计划名称

    private String planTypeName;// 计划类型

    private String lockPeriod;// 锁定期

    private String fullExpireTime;// 计划满标时间

    private String buyEndTime;// 计划截止时间

    private String liquidateShouldTime;// 计划应清算时间

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

    private String expectApr; // 预期出借利率

    private String actualApr;// 实际出借利率
}
