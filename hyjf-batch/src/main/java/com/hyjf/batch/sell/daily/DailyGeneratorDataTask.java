package com.hyjf.batch.sell.daily;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.hyjf.common.util.GetDate;

/**
 * @author xiasq
 * @version DailyGeneratorDataTask, v0.1 2018/7/26 15:09
 */
public class DailyGeneratorDataTask {
	private Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private DailyGeneratorDataService dailyGeneratorDataService;
	@Autowired
	private DailyAutoSendService dailyAutoSendService;

	public void run() {
		Date currentDate = new Date();
		String currentDateStr = GetDate.formatDate(currentDate);

		// 休息日、节假日不执行
		if (!dailyAutoSendService.isWorkdateOnSomeDay(currentDate)) {
			logger.info("节假日不发送邮件...currentDate is :{}", currentDateStr);
			return;
		}
		// 判断当天已有数据，不执行
		if (dailyGeneratorDataService.hasGeneratorDataToday()) {
			logger.info("数据已经生成，如想重复生成请删除数据...currentDate is :{}", currentDateStr);
			return;
		}
		logger.info("销售日报生成开始.......");
		long startTime = System.currentTimeMillis();
		// 初始化部门
		dailyGeneratorDataService.initDepartment();
		long startTime2 = System.currentTimeMillis();
		logger.info("初始化部门结束，耗时: {}ms", startTime2 - startTime);
		dailyGeneratorDataService.generatorSellDaily(currentDate);
		logger.info("销售日报生成结束，耗时: {}ms", System.currentTimeMillis() - startTime2);
	}

}
