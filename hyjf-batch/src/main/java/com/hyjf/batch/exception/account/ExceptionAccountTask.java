package com.hyjf.batch.exception.account;

import org.springframework.beans.factory.annotation.Autowired;

import com.hyjf.common.util.GetDate;
/**
 * 汇付账户余额与平台账户余额对比任务
 * @author ZPC
 *
 */
public class ExceptionAccountTask {
	// 判断一轮定时是否跑完,如果没跑完,不允许再跑一个
	private static Boolean isOver = true;
	@Autowired
	private ExceptionAccountService exceptionAccountService;
	
	public void run(){
		if (isOver) {
			System.out.println("汇付对账任务开始------" + GetDate.date2Str(GetDate.datetimeFormat));
			isOver = false;
			exceptionAccountService.syncHuiFuAccounts();
			isOver = true;
			System.out.println("汇付对账任务结束------" + GetDate.date2Str(GetDate.datetimeFormat));
		}
	}
}
