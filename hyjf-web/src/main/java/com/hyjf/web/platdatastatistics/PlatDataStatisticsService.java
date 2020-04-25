package com.hyjf.web.platdatastatistics;

import com.hyjf.mybatis.model.auto.BorrowUserStatistic;
import com.hyjf.mybatis.model.auto.BorrowUserStatisticExample;
import com.hyjf.mybatis.model.auto.CalculateInvestInterest;
import com.hyjf.web.BaseService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

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

    /**
     * 检索累计出借总数
     * @return
     */
    Integer selectTenderCounts();

    /**
     * 累计注册数
     * @return
     */
    Integer selectUserRegisterCounts();

    /**
     * 查询平台待还金额
     * @return
     */
    BigDecimal selectRepayTotal();

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
