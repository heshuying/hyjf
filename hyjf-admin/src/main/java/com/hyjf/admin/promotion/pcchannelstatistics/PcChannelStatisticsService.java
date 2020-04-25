package com.hyjf.admin.promotion.pcchannelstatistics;

import java.util.List;

import com.hyjf.admin.BaseService;
import com.hyjf.mybatis.model.customize.admin.PcChannelStatisticsCustomize;

/**
 * PC渠道统计
 * 
 * @author liuyang
 *
 */
public interface PcChannelStatisticsService extends BaseService {

	/**
	 * 列表件数
	 * 
	 * @param pcChannelStatisticsCustomize
	 * @return
	 */
	public Integer countList(PcChannelStatisticsCustomize pcChannelStatisticsCustomize);

	/**
	 * PC渠道统计列表数据
	 * 
	 * @param pcChannelStatisticsCustomize
	 * @return
	 */
	public List<PcChannelStatisticsCustomize> selectSumRecordList(PcChannelStatisticsCustomize pcChannelStatisticsCustomize);

	/**
	 * 列表导出
	 * 
	 * @param pcChannelStatisticsCustomize
	 * @return
	 */
	public List<PcChannelStatisticsCustomize> exportList(PcChannelStatisticsCustomize pcChannelStatisticsCustomize);

}
