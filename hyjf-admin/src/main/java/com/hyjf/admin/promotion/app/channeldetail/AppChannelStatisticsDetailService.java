package com.hyjf.admin.promotion.app.channeldetail;

import java.util.List;

import com.hyjf.admin.BaseService;
import com.hyjf.mybatis.model.auto.UtmPlat;
import com.hyjf.mybatis.model.customize.app.AppChannelStatisticsDetailCustomize;

public interface AppChannelStatisticsDetailService extends BaseService{

	/**
	 * count列表
	 * 
	 * @return
	 */
	public Integer countList(AppChannelStatisticsDetailCustomize appChannelStatisticsDetailCustomize);

	/**
	 * 获取列表
	 * 
	 * @return
	 */
	public List<AppChannelStatisticsDetailCustomize> getRecordList(AppChannelStatisticsDetailCustomize appChannelStatisticsDetailCustomize);

	/**
	 * 导出列表
	 * 
	 * @return
	 */
	public List<AppChannelStatisticsDetailCustomize> exportList(AppChannelStatisticsDetailCustomize appChannelStatisticsDetailCustomize);
	
	/**
	 * 获取app渠道列表
	 */
	public List<UtmPlat> getAppUtm();
}
