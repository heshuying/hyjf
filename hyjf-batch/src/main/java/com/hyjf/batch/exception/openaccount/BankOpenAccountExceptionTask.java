package com.hyjf.batch.exception.openaccount;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.hyjf.common.log.LogUtil;
import com.hyjf.common.util.GetDate;

/**
 * 
 * add by 2017-5-27 银行开户掉单处理
 * 
 * @author cwyang
 *
 */
public class BankOpenAccountExceptionTask {

	// 判断一轮定时是否跑完,如果没跑完,不允许再跑一个
	private static Boolean isOver = true;
	@Autowired
	private BankOpenAccountExceptionService bankOpenAccountExceptionService;

	Logger _log = LoggerFactory.getLogger(BankOpenAccountExceptionTask.class);

	public void run() {
		if (isOver) {
			_log.info("银行开户掉单跑批任务开始------" + GetDate.date2Str(GetDate.datetimeFormat));
			isOver = false;
			try {
				bankOpenAccountExceptionService.updateBankOpen();
			} catch (Exception e) {
				LogUtil.errorLog(this.getClass().getName(), "BankOpenAccountExceptionTask.run", e);
			}
			isOver = true;
			_log.info("银行开户掉单跑批任务结束------" + GetDate.date2Str(GetDate.datetimeFormat));
		}
	}
}
