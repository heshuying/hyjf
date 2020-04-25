package com.hyjf.batch.pc.statistics;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.hyjf.common.util.GetDate;
import com.hyjf.mybatis.model.auto.UtmPlat;
import com.hyjf.mybatis.model.customize.admin.PcChannelStatisticsCustomize;

/**
 * Pc渠道统计定时任务
 * 
 * @author liuyang
 *
 */
public class PcChannelStatisticsTask {
	/** 运行状态 */
	private boolean isRun = true;
	@Autowired
	private PcChannelStatisticsService pcChannelStatisticsService;

	public void run() throws Exception {
		insertStatisticsTask();
	}

	private void insertStatisticsTask() throws Exception {
		System.out.println("----------------PC渠道统计定时任务Start-------------");
		if (isRun) {
			isRun = false;
			try {
				// 当前时间 yyyy-MM-dd
				String nowDate = GetDate.date2Str(GetDate.date_sdf);
				//String nowDate = "2017-06-08";
				PcChannelStatisticsCustomize pcChannelStatisticsCustomize = new PcChannelStatisticsCustomize();
				pcChannelStatisticsCustomize.setTimeStartSrch(GetDate.getDayStart(nowDate));
				pcChannelStatisticsCustomize.setTimeEndSrch(GetDate.getDayEnd(nowDate));
				// 取得所有渠道
				List<UtmPlat> utmList = this.pcChannelStatisticsService.selectUtmPlatList();
				// 处理开始时间
				String startTime = GetDate.dateToString(new Date());
				System.out.println("处理开始时间:" + startTime);
				if (utmList != null && utmList.size() > 0) {
					for (int i = 0; i < utmList.size(); i++) {
						UtmPlat utmPlat = utmList.get(i);
						try {
							pcChannelStatisticsCustomize.setSourceIdSrch(String.valueOf(utmPlat.getSourceId()));
							boolean isInsertFlag = pcChannelStatisticsService.insertRecord(pcChannelStatisticsCustomize, nowDate, utmPlat);
							if (!isInsertFlag) {
								throw new Exception("插入PC统计数据表失败");
							}
						} catch (Exception e) {
							e.printStackTrace();
							System.out.println("PC渠道统计定时任务出错,渠道ID:" + utmPlat.getSourceId() + ",处理日期:" + nowDate);
						}
					}
				}
				// 处理结束时间
				String endTime = GetDate.dateToString(new Date());
				// 处理用时
				String consumeTime = GetDate.countTime(GetDate.stringToDate(startTime), GetDate.stringToDate(endTime));
				System.out.println("处理结束时间:" + endTime);
				System.out.println("处理用时:" + startTime + "减去" + endTime + "等于" + consumeTime);
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				isRun = true;
			}
		}
		System.out.println("----------------PC渠道统计定时任务End-------------");
	}
}
