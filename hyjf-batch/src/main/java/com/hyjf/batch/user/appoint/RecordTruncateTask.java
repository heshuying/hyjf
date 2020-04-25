package com.hyjf.batch.user.appoint;

import org.springframework.beans.factory.annotation.Autowired;
/**
 * 
 * @author: zhouxiaoshuai
 * @email: 	287424494@qq.com		
* @description: 违约分值清空定时任务
 * @version:     1 
 * @date:   2016年7月8日 下午4:06:01
 */
public class RecordTruncateTask {

	@Autowired
	private RecordTruncateService recordTruncateService;

	public void RecordTask() {
		System.out.println("----------------清空违约分值定时任务-------------");
		// 清空违约分值
		recordTruncateService.updateRecordTruncate();
		System.out.println("----------------清空违约分值定时任务结束-------------");
	}

}
