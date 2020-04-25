package com.hyjf.batch.pc.statistics;

import java.util.List;

import com.hyjf.batch.BaseService;
import com.hyjf.mybatis.model.auto.UtmPlat;
import com.hyjf.mybatis.model.customize.admin.PcChannelStatisticsCustomize;

/**
 * Pc渠道统计定时Service
 * 
 * @author liuyang
 *
 */
public interface PcChannelStatisticsService extends BaseService {

	/**
	 * 检索当天PC统计数据
	 * 
	 * @param pcChannelStatisticsCustomize
	 * @return
	 */
	public List<PcChannelStatisticsCustomize> selectPcChannelStatisticsList(PcChannelStatisticsCustomize pcChannelStatisticsCustomize);

	/**
	 * 插入PC统计数据表
	 * 
	 * @param statisticsCustomize
	 * @return
	 */
	public boolean insertRecord(PcChannelStatisticsCustomize statisticsCustomize, String nowDate,UtmPlat utmPlat);

	/**
	 * 获取所有渠道号
	 * 
	 * @return
	 */
	public List<UtmPlat> selectUtmPlatList();

}
