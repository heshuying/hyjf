package com.hyjf.batch.statistics.tzjday;

import java.util.Map;

/**
 * 
 * 此处为类说明
 * @author hsy
 * @version hyjf 1.0
 * @since hyjf 1.0 2017年3月23日
 * @see 下午3:21:29
 */
public interface TZJStatisticsDayReportService {

	void updateStatistics(String statisticDay, Map<String,Object> data);

	Map<String,Object> getStatistics(String statisticDay);

//	void doStatistics();
	
}
