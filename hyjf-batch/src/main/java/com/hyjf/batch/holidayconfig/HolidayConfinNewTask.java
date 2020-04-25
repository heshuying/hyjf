package com.hyjf.batch.holidayconfig;

import java.util.Calendar;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author xiasq
 * @version HolidayConfinNewTask, v0.1 2018/7/26 17:19
 */
public class HolidayConfinNewTask {
	private Logger logger = LoggerFactory.getLogger(HolidayConfinNewTask.class);
	@Autowired
	private HolidayConfigNewService holidayConfigNewService;

	public void run() {
		int currentYear = Calendar.getInstance().get(Calendar.YEAR);
		logger.info("日历同步开始,currentYear is : {}", currentYear);
		// 判断数据已经存在， 不重复同步
		if(holidayConfigNewService.hasHolidayConfig(currentYear)){
			logger.info("{}年数据已生成，不重复同步...", currentYear);
			return;
		}
		holidayConfigNewService.initCurrentYearConfig(currentYear);

		holidayConfigNewService.updateHolidaysConfig(currentYear);
	}
}
