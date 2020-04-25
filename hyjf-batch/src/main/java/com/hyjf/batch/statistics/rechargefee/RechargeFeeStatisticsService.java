/*
 * Copyright(c) 2012-2016 JD Pharma.Ltd. All Rights Reserved.
 */
package com.hyjf.batch.statistics.rechargefee;

import com.hyjf.batch.BaseService;
import com.hyjf.mybatis.model.customize.RechargeFeeStatisticsCustomize;

/**
 * 充值手续费垫付对账
 * @author 李深强
 */
public interface RechargeFeeStatisticsService extends BaseService{
	
	/**
	 * 根据统计时间判断数据是否存在（每天一条数据）
	 * 
	 * @return recordid
	 */
	public int isExistsRecordByDate(String staDate);
	
	/**
	 * 插入数据
	 * @param rechargeFeeStatistics
	 */
	public void insertRechargeFeeStatisticsRecord(String staDate,RechargeFeeStatisticsCustomize rechargeFeeStatisticsCustomize);
	
	/**
	 * 更新数据
	 * @param rechargeFeeStatistics
	 */
	public void updateRechargeFeeStatisticsRecord(int recordId,RechargeFeeStatisticsCustomize rechargeFeeStatisticsCustomize);
	
	/**
	 * 查询统计数据
	 * @param RechargeFeeStatisticsCustomize
	 */
    RechargeFeeStatisticsCustomize selectRechargeFeeStatistics(RechargeFeeStatisticsCustomize rechargeFeeStatisticsCustomize);

}
