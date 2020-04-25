package com.hyjf.batch.exception.investall;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 出借全部掉单异常处理
 * 
 * @author cwyang 2017-5-22
 */
public class BatchBankInvesetAllExceptionTask {

	// 判断一轮定时是否跑完,如果没跑完,不允许再跑一个
	private static Boolean isOver = true;
	@Autowired
	private BankInvesetExceptionAllService bankInvesertExceptionService;

	Logger _log = LoggerFactory.getLogger(BatchBankInvesetAllExceptionTask.class);

	public void run() {
		if (isOver) {
			_log.info("出借异常全部掉单跑批任务开始------");
			isOver = false;
			bankInvesertExceptionService.updateTender();
			isOver = true;
			_log.info("出借异常全部掉单跑批任务结束------");
		}
	}
}
