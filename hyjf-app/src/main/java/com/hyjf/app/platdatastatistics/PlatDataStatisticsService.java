package com.hyjf.app.platdatastatistics;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Service;

import com.hyjf.app.BaseService;
import com.hyjf.mybatis.model.auto.BorrowUserStatistic;
import com.hyjf.mybatis.model.auto.BorrowUserStatisticExample;
import com.hyjf.mybatis.model.auto.CalculateInvestInterest;

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
