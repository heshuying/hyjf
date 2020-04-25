package com.hyjf.batch.htj.statis;

import org.springframework.beans.factory.annotation.Autowired;
/**
 * 
 * @author: zhouxiaoshuai
 * @email: 	287424494@qq.com		
* @description: 平台数据定时任务
 * @version:     1 
 * @date:   2016年7月8日 下午4:06:01
 */
public class PlanDataInfoTask {

	@Autowired
	private PlanDataInfoService plandataInfoService;

	public void dataTask() {
		System.out.println("----------------汇添金平台数据定时任务-------------");
		// 插入出借统计表
		plandataInfoService.insertDataInfo();
		
	}

}
