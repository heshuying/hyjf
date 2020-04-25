/*
 * @Copyright: 2005-2018 www.hyjf.com. All rights reserved.
 */
package com.hyjf.mqreceiver.borrow.operationdata;

import com.hyjf.common.util.CustomConstants;
import com.hyjf.mybatis.mapper.customize.OperationReportCustomizeMapper;
import com.hyjf.mybatis.mapper.customize.web.hjh.HjhPlanCustomizeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * @author fuqiang
 * @version OperationDataServiceImpl, v0.1 2018/5/24 10:14
 */
@Service
public class OperationDataServiceImpl implements OperationDataService {

    @Autowired
    private OperationReportCustomizeMapper operationReportCustomizeMapper;

    @Autowired
    private HjhPlanCustomizeMapper hjhPlanCustomizeMapper;

    @Override
    public int countTotalInvestNum() {
        return operationReportCustomizeMapper.getTradeCount() + CustomConstants.HTJ_HTL_COUNT;
    }

    @Override
    public BigDecimal countTotalInterestAmount() {
        return operationReportCustomizeMapper.getTotalInterest();
    }

    @Override
    public BigDecimal countTotalInvestAmount() {
        return operationReportCustomizeMapper.getTotalCount();
    }

    @Override
    public List<Map<String, Object>> searchPlanStatisticData() {
        return hjhPlanCustomizeMapper.searchPlanStatisticData();
    }
}
