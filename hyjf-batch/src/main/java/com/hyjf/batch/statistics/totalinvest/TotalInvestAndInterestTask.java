package com.hyjf.batch.statistics.totalinvest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author xiasq
 * @version TotalInvestAndInterestTask, v0.1 2018/5/16 10:17
 */
public class TotalInvestAndInterestTask {

	Logger _log = LoggerFactory.getLogger(TotalInvestAndInterestTask.class);

	// 判断一轮定时是否跑完,如果没跑完,不允许再跑一个
	private static volatile Boolean isOver = true;

	@Autowired
	public TotalInvestAndInterestService totalInvestAndInterestService;

	public void run() {
		if (isOver) {
			try {
				isOver = false;

				statisticsDay();
			} finally {
				isOver = true;
			}
		}
	}

	/**
	 * 统计报表<br>
	 * 根据当前时间要获取到上个月的数据
	 *
	 *
	 */
	private void statisticsDay() {
		_log.info("开始执行统计数据...");
		try {
			totalInvestAndInterestService.execute();
		} catch (Exception e) {
			_log.error("统计数据到mongodb出错...", e);
		}
		_log.info("完成插入统计数据到mongodb...");
	}

}
