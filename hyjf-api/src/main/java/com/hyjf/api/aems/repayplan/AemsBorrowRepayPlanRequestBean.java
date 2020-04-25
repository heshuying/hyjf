/*
 * @Copyright: 2005-2018 www.hyjf.com. All rights reserved.
 */
package com.hyjf.api.aems.repayplan;

import com.hyjf.base.bean.BaseBean;

/**
 * Aems系统:查询还款计划请求Bean
 *
 * @author liuyang
 * @version AemsRepayPlanRequestBean, v0.1 2018/10/16 17:30
 */
public class AemsBorrowRepayPlanRequestBean extends BaseBean {

    // 最后还款开始时间
    private String startDate;
    // 最后还款开始时间
    private String endDate;
    // 查询类型:0:待还款 1:已还款
    private String repayType;
    // 是否分期
    private String isMonth;
    // 资产编号
    private String productId;

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getRepayType() {
        return repayType;
    }

    public void setRepayType(String repayType) {
        this.repayType = repayType;
    }

    public String getIsMonth() {
        return isMonth;
    }

    public void setIsMonth(String isMonth) {
        this.isMonth = isMonth;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }
}
