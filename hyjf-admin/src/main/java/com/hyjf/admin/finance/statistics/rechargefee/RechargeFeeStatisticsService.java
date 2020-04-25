package com.hyjf.admin.finance.statistics.rechargefee;

import java.util.List;

import com.hyjf.mybatis.model.auto.RechargeFeeBalanceStatistics;
import com.hyjf.mybatis.model.auto.RechargeFeeStatistics;
import com.hyjf.mybatis.model.customize.RechargeFeeStatisticsCustomize;

public interface RechargeFeeStatisticsService {
	
	/**
	 * 充值手续费统计记录数
	 * @param RechargeFeeStatisticsBean
	 * @return
	 */
	public Integer queryRechargeFeeStatisticsCount(RechargeFeeStatisticsBean form) ;
	
	/**
	 * 充值手续费统计 （列表）
	 * @param RechargeFeeStatisticsBean
	 * @return
	 */
	public List<RechargeFeeStatistics> queryRechargeFeeStatisticsList(RechargeFeeStatisticsBean form, int limitStart, int limitEnd) ;
	

	/**
	 * 导出列表
	 * @param RechargeFeeStatisticsBean
	 * @return
	 */
	public List<RechargeFeeStatistics> exportRechargeFeeStatisticsList(RechargeFeeStatisticsBean form) ;
	
	/**
	 * 总计查询
	 * @return
	 */
	public RechargeFeeStatisticsCustomize selectRechargeFeeStatisticsSum(RechargeFeeStatisticsBean form);


	/**
	 * 充值手续费垫付余额 查询  by 日期
	 * @return
	 */
	public List<RechargeFeeBalanceStatistics> selectFeeBalanceStatistics(String staDate);
	
	
	/**
	 * 全部列表
	 * @return
	 */
	public List<RechargeFeeStatistics> selectAllRechargeFeeStatisticsList() ;
	
	
	/**
	 * 获取最新记录
	 * @return  RechargeFeeStatistics
	 */
	public RechargeFeeStatistics selectNewRechargeFeeStatistics() ;
	
	
}

	