package com.hyjf.wechat.service.find.operationalData;

import org.springframework.stereotype.Service;

import com.hyjf.mybatis.model.auto.BorrowUserStatistic;
import com.hyjf.mybatis.model.auto.CalculateInvestInterest;
import com.hyjf.wechat.base.BaseService;

import java.math.BigDecimal;

/**
 * 平台数据统计Service
 *
 * @author liuyang
 */
@Service
public interface PlatDataStatisticsService extends BaseService {

    /**
     * 获取累计收益累计出借
     * @return
     */
    CalculateInvestInterest selectCalculateInvestInterest();

	BorrowUserStatistic selectBorrowUserStatistic();

    /**
     * 查询累计出借
     * @return
     */
    BigDecimal selectTotalInvest();

    /**
     * 查询累计收益
     * @return
     */
    BigDecimal selectTotalInterest();

    /**
     * 获取累计交易笔数
     * @return
     */
    Integer selectTotalTradeSum();
    

}
