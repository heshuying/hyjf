package com.hyjf.batch.app.promotiondetail;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.hyjf.common.util.GetDate;
import com.hyjf.mybatis.model.auto.AppChannelStatistics;
import com.hyjf.mybatis.model.customize.batch.BatchChannelStatisticsOldCustomize;

/**
 * APP渠道统计老数据修复定时 此定时只跑一次
 * 
 * @author Michael
 *
 */
public class ChannelStatisticsOldTask {
	/** 运行状态 */
	private static int isRun = 0;

	@Autowired
	private ChannelStatisticsDetailService channelStatisticsDetailService;

	public void run() {
		// 调用更新接口
		update();
	}

	private void update() {
		System.out.println("APP渠道统计老数据修复定时 start");
		if (isRun == 0) {
			isRun = 1;
			try {
				// 检索APP渠道统计明细老数据
				List<AppChannelStatistics> appChannelStatisticsList = this.channelStatisticsDetailService.selectChannelStatisticsList();
				if (appChannelStatisticsList != null && appChannelStatisticsList.size() > 0) {
					for (AppChannelStatistics appChannelStatistics : appChannelStatisticsList) {
						
						//渠道ID
						Integer sourceId = appChannelStatistics.getSourceId();
						//更新时间
						Date updateTime = appChannelStatistics.getUpdateTime();
						//取时间段
						String timeStartSrch = GetDate.getDayStart(GetDate.dateToString2(updateTime));
						String timeEndSrch = GetDate.getDayEnd(GetDate.dateToString2(updateTime));
						//存入数据
						BatchChannelStatisticsOldCustomize batchChannelStatisticsOldCustomize = new BatchChannelStatisticsOldCustomize();
						batchChannelStatisticsOldCustomize.setSourceId(String.valueOf(sourceId));
						batchChannelStatisticsOldCustomize.setTimeStartSrch(timeStartSrch);
						batchChannelStatisticsOldCustomize.setTimeEndSrch(timeEndSrch);
						//取得出借信息数据
					    batchChannelStatisticsOldCustomize = this.channelStatisticsDetailService.selectChannelStatisticsOldList(batchChannelStatisticsOldCustomize);
						
						if (batchChannelStatisticsOldCustomize != null) {
							// 更新hyjf_app_channel_statistics表
							boolean isupdateFlag = this.channelStatisticsDetailService.updateAppChannelStatistics(appChannelStatistics, batchChannelStatisticsOldCustomize);
							if (!isupdateFlag) {
								throw new Exception("更新hyjf_app_channel_statistics表失败!");
							}
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				// 定时只跑一次
				isRun = 1;
			}
		}
		System.out.println("APP渠道统计老数据修复定时 end");
	}
}
