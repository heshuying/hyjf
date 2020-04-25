package com.hyjf.mybatis.mapper.customize.app;

import java.util.List;
import java.util.Map;

import com.hyjf.mybatis.model.customize.app.AppChannelStatisticsDetailCustomize;

public interface AppChannelStatisticsDetailCustomizeMapper {

	/**
	 * 导出列表
	 * 
	 * @param AppChannelStatisticsDetailCustomize
	 * @return
	 */
	List<AppChannelStatisticsDetailCustomize> exportList(AppChannelStatisticsDetailCustomize appChannelStatisticsDetailCustomize);

	/**
	 * 后台展示列表
	 * 
	 * @param AppChannelStatisticsDetailCustomize
	 * @return
	 */
	List<AppChannelStatisticsDetailCustomize> selectList(AppChannelStatisticsDetailCustomize appChannelStatisticsDetailCustomize);

	/**
	 * 后台展示COUNT
	 * 
	 * @param AppChannelStatisticsDetailCustomize
	 * @return
	 */
	Integer countList(AppChannelStatisticsDetailCustomize appChannelStatisticsDetailCustomize);

	/**
	 * 更新相应的用户的渠道统计
	 * 
	 * @param borrow
	 * @return
	 */
	int updateAppChannelStatisticsDetail(Map<String, Object> borrow);

	/**
	 * 更新相应的用户的渠道统计
	 * 
	 * @param params
	 */
	void updateFirstAppChannelStatisticsDetail(Map<String, Object> params);

	/**
	 * 更新huiyingdai_utm_reg相应的用户信息
	 * 
	 * @param params
	 */
	void updateFirstUtmReg(Map<String, Object> params);
}