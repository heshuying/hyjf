package com.hyjf.batch.bank.borrow.creditend;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.hyjf.bank.service.user.creditend.CreditEndService;

/**
 * 批次结束债权请求
 * @author liubin
 *
 */
public class CreditEndTask {


	Logger _log = LoggerFactory.getLogger(CreditEndTask.class);

	/** 运行状态 */
	private static int isRun = 0;

	@Autowired
	CreditEndService creditEndService;

	public void run() {
		// 调用还款接口
		taskAssign();
	}

	/**
	 * 对批次结束债权请求进行分派
	 *
	 * @return
	 */
	private void taskAssign() {
		if (isRun == 1) {
			return;
		}
		isRun = 1;
		_log.info("-----------------批次结束债权请求开始------------------");
		try {
			// 批次结束债权
			Boolean result = this.creditEndService.updateBatchCreditEnd();
			if (result){
				_log.info("-----------------批次结束债权请求成功---------------");
			}else{
				_log.error("-----------------批次结束债权请求失败---------------");
			}
			return;
		} catch (Exception e) {
			_log.error("-----------------批次结束债权请求异常-------------------", e);
		} finally {
			isRun = 0;
		}
	}
}