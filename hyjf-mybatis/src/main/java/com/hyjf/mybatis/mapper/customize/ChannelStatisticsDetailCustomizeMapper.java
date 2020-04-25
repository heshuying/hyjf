package com.hyjf.mybatis.mapper.customize;

import java.util.List;

import com.hyjf.mybatis.model.customize.ChannelStatisticsDetailCustomize;

public interface ChannelStatisticsDetailCustomizeMapper {
	
	/**
	 * 导出列表
	 * 
	 * @param AppChannelStatisticsDetailCustomize
	 * @return
	 */
	List<ChannelStatisticsDetailCustomize> exportList(ChannelStatisticsDetailCustomize channelStatisticsDetailCustomize);
	
	/**
	 * 后台展示列表
	 * 
	 * @param AppChannelStatisticsDetailCustomize
	 * @return
	 */
	List<ChannelStatisticsDetailCustomize> selectList(ChannelStatisticsDetailCustomize channelStatisticsDetailCustomize);

	/**
	 * 后台展示COUNT
	 * 
	 * @param AppChannelStatisticsDetailCustomize   
	 * @return
	 */
	Integer countList(ChannelStatisticsDetailCustomize channelStatisticsDetailCustomize);


}