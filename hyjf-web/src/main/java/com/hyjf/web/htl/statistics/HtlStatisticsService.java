package com.hyjf.web.htl.statistics;

import com.hyjf.mybatis.model.customize.web.WebHtlStatisticsCustomize;
import com.hyjf.web.BaseService;

public interface HtlStatisticsService extends BaseService {

	/**
	 * 查询htl统计数据
	 * @return
	 */
	WebHtlStatisticsCustomize searchTotalStatistics();


}
