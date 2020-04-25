package com.hyjf.admin.promotion.channeldetail;

import java.util.List;

import com.hyjf.admin.BaseService;
import com.hyjf.mybatis.model.auto.UtmPlat;
import com.hyjf.mybatis.model.customize.ChannelStatisticsDetailCustomize;

public interface ChannelStatisticsDetailService extends BaseService {

	/**
	 * count列表
	 * 
	 * @return
	 */
	public Integer countList(ChannelStatisticsDetailCustomize channelStatisticsDetailCustomize);

	/**
	 * 获取列表
	 * 
	 * @return
	 */
	public List<ChannelStatisticsDetailCustomize> getRecordList(ChannelStatisticsDetailCustomize channelStatisticsDetailCustomize);

	/**
	 * 导出列表
	 * 
	 * @return
	 */
	public List<ChannelStatisticsDetailCustomize> exportList(ChannelStatisticsDetailCustomize channelStatisticsDetailCustomize);

	/**
	 * 获取app渠道列表
	 */
	public List<UtmPlat> getPCUtm();

}
