package com.hyjf.mybatis.model.customize.app;

import java.io.Serializable;

/**
 * 
 * App债转还款计划Bean
 * @author liuyang
 * @version hyjf 1.0
 * @since hyjf 1.0 2016年8月5日
 * @see 下午1:47:01
 */
public class AppTenderCreditRepayPlanListCustomize implements Serializable {

    /**
     * 序列化id
     */
    private static final long serialVersionUID = -2869301136107487471L;

    // 类型：本息还是利息
    private String repayName;

    // 金额
    private String account;

    // 时间
    private String repayTime;

    // 还款状态
    private String repayStatus;

    /**
     * 构造方法
     */
    public AppTenderCreditRepayPlanListCustomize() {
        super();
    }

    public String getRepayName() {
        return repayName;
    }

    public void setRepayName(String repayName) {
        this.repayName = repayName;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getRepayTime() {
        return repayTime;
    }

    public void setRepayTime(String repayTime) {
        this.repayTime = repayTime;
    }

    public String getRepayStatus() {
        return repayStatus;
    }

    public void setRepayStatus(String repayStatus) {
        this.repayStatus = repayStatus;
    }

}
