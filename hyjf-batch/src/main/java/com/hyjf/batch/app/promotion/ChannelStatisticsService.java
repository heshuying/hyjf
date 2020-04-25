package com.hyjf.batch.app.promotion;

import java.util.List;

import com.hyjf.mybatis.model.auto.UtmPlat;
import com.hyjf.mybatis.model.customize.app.AppChannelStatisticsCustomize;

public interface ChannelStatisticsService {

	/**
	 * 获取列表
	 * 
	 * @return
	 */
	public AppChannelStatisticsCustomize selectAppChannelStatistics(AppChannelStatisticsCustomize channelStatisticsCustomize);

	/**
	 * 插入记录
	 * 
	 * @param channelStatisticsCustomize
	 */
	public void insertRecord(AppChannelStatisticsCustomize channelStatisticsCustomize);

	/**
	 * 检索所有App渠道
	 * 
	 * @return
	 */
	public List<UtmPlat> selectUtmPlat();
}
