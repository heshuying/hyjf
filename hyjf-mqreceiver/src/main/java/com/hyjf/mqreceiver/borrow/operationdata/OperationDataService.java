/*
 * @Copyright: 2005-2018 www.hyjf.com. All rights reserved.
 */
package com.hyjf.mqreceiver.borrow.operationdata;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * @author fuqiang
 * @version OperationDataService, v0.1 2018/5/24 10:11
 */
public interface OperationDataService {
    /**
     * 查询累计出借笔数
     *
     * @return
     */
    int countTotalInvestNum();

    /**
     * 查询累计收益
     *
     * @return
     */
    BigDecimal countTotalInterestAmount();

    /**
     * 查询累计出借总额
     *
     * @return
     */
    BigDecimal countTotalInvestAmount();

    /**
     * 查询汇计划数据
     */
    List<Map<String, Object>> searchPlanStatisticData();
}
