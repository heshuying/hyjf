package com.hyjf.batch.data;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
/**
 * 
 * @author: zhouxiaoshuai
 * @email: 	287424494@qq.com		
* @description: 平台数据定时任务
 * @version:     1 
 * @date:   2016年7月8日 下午4:06:01
 */
public class DataInfoTask {

	@Autowired
	private DataInfoService dataInfoService;

	public void dataTask() {
		System.out.println("----------------平台数据定时任务-------------");
		// 插入出借统计表
		dataInfoService.insertDataInfo();
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("dd");
		String strdate = sdf.format(date);
		if (strdate.equals("01")) {
			// 插入上月出借记录
			dataInfoService.insertAYearTenderInfo();
		}
	}

}
