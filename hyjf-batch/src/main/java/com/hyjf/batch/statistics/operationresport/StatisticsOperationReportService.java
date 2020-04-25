package com.hyjf.batch.statistics.operationresport;

import com.hyjf.mybatis.model.auto.BorrowUserStatistic;

import java.util.Calendar;




public interface StatisticsOperationReportService {
	
	/**
	 * 获取 性别，区域，年龄的统计数据
	 * @param cal 统计日期
	 * @return
	 */
	public void insertOperationGroupData(Calendar cal);

	/**
	 * 插入出借类的信息
	 * @param cal
	 */
	public Calendar insertOperationData(Calendar cal) ;

	/**
	 * 检索运营统计数据
	 * @return
	 */
	public BorrowUserStatistic selectBorrowUserStatistic();
	
//	/**
//	 * 按月统计平台的交易总额
//	 * 
//	 * @param beginDate
//	 *            统计月的第一天
//	 * @param endDate
//	 *            统计月的最后一天
//	 * @return
//	 */
//	public BigDecimal getAccountByMonth(Date beginDate, Date endDate);
//
//	/**
//	 * 按月统计交易笔数
//	 * @param beginDate 统计月的第一天
//	 * @param endDate	统计月的最后一天
//	 * @return
//	 */
//	public int getTradeCountByMonth(Date beginDate,Date endDate);
//	
//	/**
//	 * 统计出借人总数，截至日期为上个月的最后一天
//	 * @param date 上个月的最后一天
//	 * @return
//	 */
//	public int getTenderCount(Date date);
//	
//	/**
//	 * 统计累计出借总数，截至日期为上个月的最后一天
//	 * @param date 上个月的最后一天
//	 * @param date
//	 * @return
//	 */
//	public BigDecimal getTotalCount(Date date);
//
//	/**
//	 * 按月统计平均满标时间
//	 * @param beginDate 统计月的第一天
//	 * @param endDate	统计月的最后一天
//	 * @return
//	 */
//	public long getFullBillAverageTimeByMonth(Date beginDate,Date endDate);
//	/**
//	 * 统计所有待偿金额，截至日期为上个月的最后一天
//	 * @param date 上个月的最后一天
//	 * @return
//	 */
//	public BigDecimal getRepayTotal(Date date);
//	/**
//	 * 
//	 * @param date 上个月的最后一天
//	 * @param firstAge  年龄下限
//	 * @param endAge	年龄上限
//	 * @return
//	 */
//	public int getTenderAgeByRange(Date date,int firstAge,int endAge);
//	
//	/**
//	 * 按照性别统计出借人的分布
//	 * @param date 上个月的最后一天
//	 */
//	public List<TenderSexCount>  getTenderSexGroupBy(Date date);
//	
//	/**
//	 * 按照省份统计出借人的分布
//	 * @param date 上个月的最后一天
//	 */
//	public List<TenderCityCount> getTenderCityGroupBy(Date date);
}
