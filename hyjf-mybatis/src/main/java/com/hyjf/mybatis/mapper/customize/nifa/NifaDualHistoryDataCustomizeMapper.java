/*
 * @Copyright: 2005-2018 www.hyjf.com. All rights reserved.
 */
package com.hyjf.mybatis.mapper.customize.nifa;

import com.hyjf.mybatis.model.auto.BorrowRepayPlan;

import java.util.List;

/**
 * @author PC-LIUSHOUYI
 * @version NifaDualHistoryDataCustomizeMapper, v0.1 2018/12/12 17:07
 */
public interface NifaDualHistoryDataCustomizeMapper {

    /**
     * 获取当天放款数据
     *
     * @param historyData
     * @return
     */
    List<String> selectBorrowByHistoryData(String historyData);

    /**
     * 获取当天到期还款的还款数据
     *
     * @param historyData
     * @return
     */
    List<String> selectBorrowRepayByHistoryData(String historyData);

    /**
     * 获取当天分期还款的还款数据
     *
     * @param historyData
     * @return
     */
    List<BorrowRepayPlan> selectBorrowRepayPlanByHistoryData(String historyData);

    /**
     * 获取当天散标债转完成的数据
     *
     * @param historyData
     * @return
     */
    List<String> selectBorrowCreditByHistoryData(String historyData);

    /**
     * 获取当天计划债转完成的数据
     *
     * @param historyData
     * @return
     */
    List<String> selectHjhDebtCreditByHistoryData(String historyData);
}
