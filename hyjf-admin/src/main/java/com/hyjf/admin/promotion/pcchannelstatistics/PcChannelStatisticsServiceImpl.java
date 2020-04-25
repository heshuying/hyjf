package com.hyjf.admin.promotion.pcchannelstatistics;

import java.util.List;

import org.springframework.stereotype.Service;

import com.hyjf.admin.BaseServiceImpl;
import com.hyjf.mybatis.model.customize.admin.PcChannelStatisticsCustomize;

/**
 * PC渠道统计Service
 * 
 * @author liuyang
 *
 */
@Service
public class PcChannelStatisticsServiceImpl extends BaseServiceImpl implements PcChannelStatisticsService {

	/**
	 * 列表件数
	 * 
	 * @param pcChannelStatisticsCustomize
	 * @return
	 */
	@Override
	public Integer countList(PcChannelStatisticsCustomize pcChannelStatisticsCustomize) {
		return pcChannelStatisticsCustomizeMapper.countList(pcChannelStatisticsCustomize);
	}

	/**
	 * PC渠道统计列表数据
	 * 
	 * @param pcChannelStatisticsCustomize
	 * @return
	 */
	@Override
	public List<PcChannelStatisticsCustomize> selectSumRecordList(PcChannelStatisticsCustomize pcChannelStatisticsCustomize) {
		return pcChannelStatisticsCustomizeMapper.selectSumRecordList(pcChannelStatisticsCustomize);
	}

	/**
	 * 列表导出
	 * 
	 * @param pcChannelStatisticsCustomize
	 * @return
	 */
	@Override
	public List<PcChannelStatisticsCustomize> exportList(PcChannelStatisticsCustomize pcChannelStatisticsCustomize) {
		return pcChannelStatisticsCustomizeMapper.exportList(pcChannelStatisticsCustomize);
	}

}
