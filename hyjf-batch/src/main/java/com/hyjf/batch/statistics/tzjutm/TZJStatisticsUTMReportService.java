package com.hyjf.batch.statistics.tzjutm;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;

/**
 * 
 * 此处为类说明
 * @author hsy
 * @version hyjf 1.0
 * @since hyjf 1.0 2017年3月23日
 * @see 下午3:21:29
 */
public interface TZJStatisticsUTMReportService {

	void updateStatistics(String statisticDay, Map<String,List<Map<String, Object>>> data) throws NoSuchMethodException, SecurityException,
			IllegalAccessException, IllegalArgumentException, InvocationTargetException;

	Map<String,List<Map<String, Object>>> getStatistics(String statisticDay);


}
