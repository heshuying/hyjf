package com.hyjf.web.htl.statistics;

import org.springframework.stereotype.Service;

import com.hyjf.mybatis.model.customize.web.WebHtlStatisticsCustomize;
import com.hyjf.web.BaseServiceImpl;

@Service
public class HtlStatisticsServiceImpl extends BaseServiceImpl implements HtlStatisticsService {

	/**
	 * 查询统计数据
	 */
	@Override
	public WebHtlStatisticsCustomize searchTotalStatistics() {
		WebHtlStatisticsCustomize htlStatistics=webHtlStatisticsCustomizeMapper.countHtlTotalStatistics();
		return htlStatistics;
	}

}
