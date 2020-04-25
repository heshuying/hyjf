/*
 * @Copyright: 2005-2018 www.hyjf.com. All rights reserved.
 */
package com.hyjf.mybatis.mapper.customize;

import com.hyjf.mybatis.model.customize.OperationReportInfoCustomize;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author yinhui
 * @version OperationReportInfoCustomizeMapper, v0.1 2018/6/20 10:09
 */
public interface OperationReportInfoCustomizeMapper {

    /**
     * 业绩总览
     */
    List<OperationReportInfoCustomize> getPerformanceSum();

    /**
     * 当月、季、半年、全年业绩  下面的  成交金额,根据月份计算
     *
     * @param startMonth 开始月份
     * @param endMonth   结束月份
     * @return
     */
    List<OperationReportInfoCustomize> getMonthDealMoney(@Param("startMonth") int startMonth, @Param("endMonth") int endMonth);

    /**
     * 今年这个时候到手收益 和 去年这个时候到手收益 和  预期收益率
     *
     * @param intervalMonth 今年间隔月份
     * @param startMonth    去年开始月份
     * @param endMonth      去年结束月份
     * @return
     */
    List<OperationReportInfoCustomize> getRevenueAndYield(@Param("intervalMonth") int intervalMonth, @Param("startMonth") int startMonth, @Param("endMonth") int endMonth);

    /**
     * 充值金额、充值笔数
     *
     * @param intervalMonth 今年间隔月份
     */
    List<OperationReportInfoCustomize> getRechargeMoneyAndSum(@Param("intervalMonth") int intervalMonth);

    /**
     * 渠道分析 ，成交笔数
     *
     * @param intervalMonth 今年间隔月份
     * @return
     */
    List<OperationReportInfoCustomize> getCompleteCount(@Param("intervalMonth") int intervalMonth);

    /**
     * 借款期限
     *
     * @param intervalMonth 今年间隔月份
     * @return
     */
    List<OperationReportInfoCustomize> getBorrowPeriod(@Param("intervalMonth") int intervalMonth);

    /**
     * 用户分析 - 性别分布
     *
     * @param intervalMonth 今年间隔月份
     * @return
     */
    List<OperationReportInfoCustomize> getSexDistribute(@Param("intervalMonth") int intervalMonth);

    /**
     * 用户分析 - 年龄分布
     *
     * @param intervalMonth 今年间隔月份
     * @return
     */
    List<OperationReportInfoCustomize> getAgeDistribute(@Param("intervalMonth") int intervalMonth);

    /**
     * 用户分析 - 金额分布
     *
     * @param intervalMonth 今年间隔月份
     * @return
     */
    List<OperationReportInfoCustomize> getMoneyDistribute(@Param("intervalMonth") int intervalMonth);

    /**
     * 十大出借人
     *
     * @param intervalMonth 今年间隔月份
     * @return
     */
    List<OperationReportInfoCustomize> getTenMostMoney(@Param("intervalMonth") int intervalMonth);

    /**
     * 超活跃，出借笔数最多
     *
     * @param intervalMonth 今年间隔月份
     * @return
     */
    List<OperationReportInfoCustomize> getOneInvestMost(@Param("intervalMonth") int intervalMonth);

    /**
     * 大赢家，收益最高
     *
     * @param intervalMonth 今年间隔月份
     * @return
     */
    List<OperationReportInfoCustomize> getOneInterestsMost(@Param("intervalMonth") int intervalMonth);

    /**
     * 通过用户ID查询 用户年龄，用户地区
     *
     * @param userId 用户ID
     * @return
     */
    OperationReportInfoCustomize getUserAgeAndArea(@Param("userId") Integer userId);

}
