package com.hyjf.admin.promotion.app.channel;

import java.util.List;

import com.hyjf.admin.BaseService;
import com.hyjf.mybatis.model.customize.app.AppChannelStatisticsCustomize;

public interface AppChannelStatisticsService extends BaseService{

	/**
	 * count列表
	 * 
	 * @return
	 */
	public Integer countSumList(AppChannelStatisticsCustomize appChannelStatisticsCustomize);

	/**
	 * 获取列表
	 * 
	 * @return
	 */
	public List<AppChannelStatisticsCustomize> getSumRecordList(AppChannelStatisticsCustomize appChannelStatisticsCustomize);

	/**
	 * 导出列表
	 * 
	 * @return
	 */
	public List<AppChannelStatisticsCustomize> exportList(AppChannelStatisticsCustomize appChannelStatisticsCustomize);
}
