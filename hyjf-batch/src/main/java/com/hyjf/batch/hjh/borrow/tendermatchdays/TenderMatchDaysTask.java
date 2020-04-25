package com.hyjf.batch.hjh.borrow.tendermatchdays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 计算自动出借的匹配期(每日)
 * @author liubin
 * 汇计划三期
 */
public class TenderMatchDaysTask {
	
	Logger _log = LoggerFactory.getLogger(TenderMatchDaysTask.class);
	
	/** 运行状态 */
	private static int isRun = 0;

	@Autowired
	TenderMatchDaysService tenderMatchDaysService;
	
	/**
	 * 调用任务实际方法接口
	 */
	public void run() {
		process();
	}

	/**
	 * 计算自动出借的匹配期任务(每日)
	 *
	 * @return
	 */
	private boolean process() {
		if (isRun == 0) {
			_log.info("计算自动出借的匹配期(每日)任务 开始... ");
			
			isRun = 1;
			try {
				// 更新未进入锁定期的计划订单的匹配期hjhaccede
				if (!this.tenderMatchDaysService.updateMatchDays()) {
					_log.error("计算自动出借的匹配期(每日)任务 失败。 ");
				};
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				isRun = 0;
			}
			_log.info("计算自动出借的匹配期(每日)任务 结束... ");
		}else{
			_log.info("计算自动出借的匹配期(每日)任务 正在运行... ");
		}
		
		return false;
	}
}
