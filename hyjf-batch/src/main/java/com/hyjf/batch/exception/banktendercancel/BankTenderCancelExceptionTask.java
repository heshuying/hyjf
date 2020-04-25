package com.hyjf.batch.exception.banktendercancel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class BankTenderCancelExceptionTask {

	// 判断一轮定时是否跑完,如果没跑完,不允许再跑一个
		private static Boolean isOver = true;
		@Autowired
		private BankTenderCancelExceptionService service;
		
		Logger _log = LoggerFactory.getLogger(BankTenderCancelExceptionTask.class);
		public void run(){
			if (isOver) {
				_log.info("出借异常数据优化任务开始------");
				isOver = false;
				try {
					service.start();
				} catch (Exception e) {
					_log.error("=========出借异常数据优化处理异常!" + e.getMessage());
				}
				isOver = true;
				_log.info("出借异常跑批任务结束------");
			}
		}
}
