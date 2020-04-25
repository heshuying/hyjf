package com.hyjf.batch.exception.bankaccountcheck;

import org.springframework.beans.factory.annotation.Autowired;

import com.hyjf.common.util.GetDate;

public class BankAccountCheckTask {
	// 判断一轮定时是否跑完,如果没跑完,不允许再跑一个
		private static Boolean isOver = true;
		@Autowired
		private BankAccountCheckService bankAccountCheckService;
		
		public void run(){
			if (isOver) {
				System.out.println("银行对账任务开始------" + GetDate.date2Str(GetDate.datetimeFormat));
				isOver = false;
				bankAccountCheckService.updateAccountCheck();
				isOver = true;
				System.out.println("银行对账任务结束------" + GetDate.date2Str(GetDate.datetimeFormat));
			}
		}
}
