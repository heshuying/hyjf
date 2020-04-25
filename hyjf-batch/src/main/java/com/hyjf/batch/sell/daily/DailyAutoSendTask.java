package com.hyjf.batch.sell.daily;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.hyjf.mybatis.model.auto.SellDailyDistribution;
import org.springframework.util.CollectionUtils;

/**
 * @author xiasq
 * @version DailyAutoSendTask, v0.1 2018/7/20 15:00
 */
public class DailyAutoSendTask {
	private Logger logger = LoggerFactory.getLogger(getClass());
	@Autowired
	private DailyAutoSendService dailyAutoSendService;

	public void run() {

		List<SellDailyDistribution> list = dailyAutoSendService.listSellDailyDistribution();
		if (CollectionUtils.isEmpty(list)) {
			logger.info("SellDailyDistribution 没有可用的配置...");
			return;
		}

		for (SellDailyDistribution sellDailyDistribution : list) {
			switch (sellDailyDistribution.getTimePoint()) {
			case 1:
				logger.info("工作日...");
				// 每个工作日
				// 判断当天是工作日
				if (dailyAutoSendService.isWorkdateOnSomeDay(new Date())) {
					dailyAutoSendService.sendMail(sellDailyDistribution);
				}
				break;
			case 2:
				// 每天
				logger.info("每日...");
				dailyAutoSendService.sendMail(sellDailyDistribution);
				break;
			case 3:
				logger.info("每月第一个工作日...");
				// 每月第一个工作日
				if (dailyAutoSendService.isTodayFirstWorkdayOnMonth()) {
					dailyAutoSendService.sendMail(sellDailyDistribution);
				}
				break;
			default:
				throw new RuntimeException("错误的配置..");
			}
		}
	}
}
