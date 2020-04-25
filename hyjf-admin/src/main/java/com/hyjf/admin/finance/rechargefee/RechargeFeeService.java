package com.hyjf.admin.finance.rechargefee;

import java.util.List;

import com.hyjf.mybatis.model.auto.RechargeFeeReconciliation;

public interface RechargeFeeService {
	
	/**
	 * 充值手续费对账记录数
	 * @param RechargeFeeStatisticsBean
	 * @return
	 */
	public Integer queryRechargeFeeCount(RechargeFeeBean form) ;
	
	/**
	 * 充值手续费对账 （列表）
	 * @param RechargeFeeStatisticsBean
	 * @return
	 */
	public List<RechargeFeeReconciliation> queryRechargeFeeList(RechargeFeeBean form, int limitStart, int limitEnd) ;
	

	/**
	 * 导出列表
	 * @param RechargeFeeStatisticsBean
	 * @return
	 */
	public List<RechargeFeeReconciliation> exportRechargeFeeList(RechargeFeeBean form) ;

}

	