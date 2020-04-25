package com.hyjf.mybatis.mapper.customize;

import java.math.BigDecimal;

import java.util.Date;
import java.util.List;


import com.hyjf.mybatis.model.customize.TenderSexCount;
import com.hyjf.mybatis.model.customize.TenderCityCount;

/**
 * 统计运营报告的相关数据
 * 
 * @author zx
 *
 */
public interface OperationReportCustomizeMapper {
	/**
	 * 按月统计平台的交易总额
	 * 
	 * @param beginDate
	 *            统计月的第一天
	 * @param endDate
	 *            统计月的最后一天
	 * @return
	 */
	BigDecimal getAccountByMonth(Date beginDate, Date endDate);

	/**
	 * 按月统计交易笔数
	 * @param beginDate 统计月的第一天
	 * @param endDate	统计月的最后一天
	 * @return
	 */
	int getTradeCountByMonth(Date beginDate,Date endDate);
	/**
	 * 累计交易笔数
	 * @return
	 */
	int getTradeCount();
	/**
	 * 统计出借人总数，截至日期为上个月的最后一天
	 * @param date 上个月的最后一天
	 * @return
	 */
	int getTenderCount(Date date);
	
	/**
	 * 统计累计出借总数，
	 * 
	 * @return
	 */
	BigDecimal getTotalCount();
	/**
	 * 统计用户累计收益
	 * @return
	 */
	BigDecimal getTotalInterest();
	 

	/**
	 * 平均满标时间
	 * @param date 统计月的最后一天
	 * @return
	 */
	float getFullBillAverageTime(Date date);
	/**
	 * 统计所有待偿金额，截至日期为上个月的最后一天
	 * @param date 上个月的最后一天
	 * @return
	 */
	BigDecimal getRepayTotal(Date date);
	/**
	 * 
	 * @param date 上个月的最后一天
	 * @param firstAge  年龄下限
	 * @param endAge	年龄上限
	 * @return
	 */
	int getTenderAgeByRange(Date date,int firstAge,int endAge);
	
	/**
	 * 按照性别统计出借人的分布
	 * @param date 上个月的最后一天
	 */
	List<TenderSexCount>  getTenderSexGroupBy(Date date);
	
	/**
	 * 按照省份统计出借人的分布
	 * @param date 上个月的最后一天
	 */
	List<TenderCityCount> getTenderCityGroupBy(Date date);
	
	/**
	 * 借贷笔数 
	 */
	int getLoanNum(Date date);

    /**
     * 借贷余额笔数
     * @param date
     * @return
     */
	int getLoanBalanceNum(Date date);
	/**
	 * 人均出借金额
	 */
	BigDecimal getPerInvestTotal();
	
	/**
	 * 获取截至日期的出借金额
	 */
	double getInvestLastDate(Date date);
}
