package com.hyjf.batch.exception.transpassword;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 
 * @author yangchangwei 出借调单异常处理 add by 2017-5-11
 *
 */
public class BatchBankTransPasswordExceptionTask {

	// 判断一轮定时是否跑完,如果没跑完,不允许再跑一个
	private static Boolean isOver = true;

	@Autowired
	private BankTransPasswordExceptionService bankTransPasswordExceptionService;

	Logger _log = LoggerFactory.getLogger(BatchBankTransPasswordExceptionTask.class);

	public void run() {
		if (isOver) {
			_log.info("设置交易密码异常跑批任务开始");
			isOver = false;
			bankTransPasswordExceptionService.insertIsSetPassword();
			isOver = true;
			_log.info("设置交易密码异常跑批任务结束");
		}
	}

}
