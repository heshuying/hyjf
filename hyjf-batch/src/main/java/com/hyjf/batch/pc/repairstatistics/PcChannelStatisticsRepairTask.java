package com.hyjf.batch.pc.repairstatistics;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.hyjf.common.util.GetDate;
import com.hyjf.mybatis.model.customize.admin.PcChannelStatisticsCustomize;

/**
 * PCs渠道统计老数据修复定时任务(此定时上线前只跑一次)
 * 
 * @author liuyang
 *
 */
public class PcChannelStatisticsRepairTask {
	/** 运行状态 */
	private boolean isRun = true;
	@Autowired
	private PcChannelStatisticsRepairService pcChannelStatisticsRepairService;

	public void run() throws Exception {
		// 老数据修复开始日期
		String startDateStr = "2017-05-12 00:00:00";
		String endDateStr = "2017-05-16 23:59:59";
		Date startDate = GetDate.stringToDate(startDateStr);
		Date endDate = GetDate.stringToDate(endDateStr);
		// 处理开始时间
		String startTime = GetDate.dateToString(new Date());
		System.out.println("老数据修复开始: " + startTime);
		while (startDate.before(endDate)) {
			insertStatisticsTask(startDate);
			startDate = GetDate.getSomeDayBeforeOrAfter(startDate, 1);
		}
		String endTime = GetDate.dateToString(new Date());
		// 处理用时
		String consumeTime = GetDate.countTime(GetDate.stringToDate(startTime), GetDate.stringToDate(endTime));
		System.out.println("老数据修复结束: " + endTime);
		System.out.println("老数据修复耗时: " + consumeTime);
	}

	private void insertStatisticsTask(Date startDate) throws Exception {
		System.out.println("----------------PC渠道统计数据修复定时任务Start-------------");
		if (isRun) {
			isRun = false;
			PcChannelStatisticsCustomize pcChannelStatisticsCustomize = new PcChannelStatisticsCustomize();
			// 当前时间 yyyy-MM-dd
			// 老数据处理开始时间:2013-12-01
			String nowDate = GetDate.date2Str(startDate, GetDate.date_sdf);
			pcChannelStatisticsCustomize.setTimeStartSrch(GetDate.getDayStart(nowDate));
			pcChannelStatisticsCustomize.setTimeEndSrch(GetDate.getDayEnd(nowDate));
			// 处理开始时间
			String startTime = GetDate.dateToString(new Date());
			System.out.println("处理开始时间:" + startTime);
			List<PcChannelStatisticsCustomize> resultList = pcChannelStatisticsRepairService.selectPcChannelStatisticsList(pcChannelStatisticsCustomize);
			if (resultList != null && resultList.size() > 0) {
				for (PcChannelStatisticsCustomize statisticsCustomize : resultList) {
					boolean isInsertFlag = pcChannelStatisticsRepairService.insertRecord(statisticsCustomize, nowDate);
					if (!isInsertFlag) {
						throw new Exception("插入PC统计数据表失败");
					}
				}
			}
			// 处理结束时间
			String endTime = GetDate.dateToString(new Date());
			// 处理用时
			String consumeTime = GetDate.countTime(GetDate.stringToDate(startTime), GetDate.stringToDate(endTime));
			System.out.println("处理结束时间:" + endTime);
			System.out.println("处理用时: " + startTime + " 减去" + endTime + " 等于" + consumeTime);
			isRun = true;
		}
		System.out.println("----------------PC渠道统计数据修复定时任务End-------------");
	}
}
