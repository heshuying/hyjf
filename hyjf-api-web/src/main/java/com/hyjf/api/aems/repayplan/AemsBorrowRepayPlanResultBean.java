/*
 * @Copyright: 2005-2018 www.hyjf.com. All rights reserved.
 */
package com.hyjf.api.aems.repayplan;

import com.hyjf.base.bean.BaseResultBean;
import com.hyjf.mybatis.model.customize.apiweb.aems.AemsBorrowRepayPlanCustomize;

import java.util.List;

/**
 * AEMS系统:查询还款计划返回Bean
 *
 * @author liuyang
 * @version AemsRepayPlanResultBean, v0.1 2018/10/16 17:32
 */
public class AemsBorrowRepayPlanResultBean extends BaseResultBean {

    private List<AemsBorrowRepayPlanCustomize> detailList;

    private Integer totalCounts;

    public List<AemsBorrowRepayPlanCustomize> getDetailList() {
        return detailList;
    }

    public void setDetailList(List<AemsBorrowRepayPlanCustomize> detailList) {
        this.detailList = detailList;
    }

    public Integer getTotalCounts() {
        return totalCounts;
    }

    public void setTotalCounts(Integer totalCounts) {
        this.totalCounts = totalCounts;
    }
}
