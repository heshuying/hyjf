package com.hyjf.admin.promotion.app.channel;

import java.util.List;

import org.springframework.stereotype.Service;

import com.hyjf.admin.BaseServiceImpl;
import com.hyjf.mybatis.model.customize.app.AppChannelStatisticsCustomize;

@Service
public class AppChannelStatisticsServiceImpl extends BaseServiceImpl implements AppChannelStatisticsService {


	/**
	 * 获取列表数
	 * @param appChannelStatisticsCustomize
	 * @return
	 * @author Michael
	 */
		
	@Override
	public Integer countSumList(AppChannelStatisticsCustomize appChannelStatisticsCustomize) {
		return appChannelStatisticsCustomizeMapper.countSumList(appChannelStatisticsCustomize);
			
	}

	/**
	 * 获取列表
	 * @param appChannelStatisticsCustomize
	 * @return
	 * @author Michael
	 */
		
	@Override
	public List<AppChannelStatisticsCustomize> getSumRecordList(
			AppChannelStatisticsCustomize appChannelStatisticsCustomize) {
		return appChannelStatisticsCustomizeMapper.selectSumList(appChannelStatisticsCustomize);
			
	}

	/**
	 * 导出报表
	 * @param appChannelStatisticsCustomize
	 * @return
	 * @author Michael
	 */
		
	@Override
	public List<AppChannelStatisticsCustomize> exportList(AppChannelStatisticsCustomize appChannelStatisticsCustomize) {
		return appChannelStatisticsCustomizeMapper.exportList(appChannelStatisticsCustomize);
			
	}
}
