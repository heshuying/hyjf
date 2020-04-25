package com.hyjf.batch.pc.repairstatistics;

import java.util.List;

import com.hyjf.batch.BaseService;
import com.hyjf.mybatis.model.customize.admin.PcChannelStatisticsCustomize;

/**
 * Pc渠道统计定时Service
 * 
 * @author liuyang
 *
 */
public interface PcChannelStatisticsRepairService extends BaseService {

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
	 * @param nowDate
	 * @return
	 */
	public boolean insertRecord(PcChannelStatisticsCustomize statisticsCustomize, String nowDate);

}
