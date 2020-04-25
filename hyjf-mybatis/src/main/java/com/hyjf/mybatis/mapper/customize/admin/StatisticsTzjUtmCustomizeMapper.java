package com.hyjf.mybatis.mapper.customize.admin;

import java.util.List;

import com.hyjf.mybatis.model.customize.admin.StatisticsTzjUtmCustomize;

public interface StatisticsTzjUtmCustomizeMapper {


	/**
	 * 导出列表
	 * 
	 * @param channelCountCustomize
	 * @return
	 */
	List<StatisticsTzjUtmCustomize> exportList(StatisticsTzjUtmCustomize channelStatisticsCustomize);
	
	/**
	 * 后台展示列表
	 * 
	 * @param channelCustomize
	 * @return
	 */
	List<StatisticsTzjUtmCustomize> selectSumList(StatisticsTzjUtmCustomize channelStatisticsCustomize);

	/**
	 * 后台展示COUNT
	 * 
	 * @param channelCustomize
	 * @return
	 */
	Integer countSumList(StatisticsTzjUtmCustomize channelStatisticsCustomize);


}