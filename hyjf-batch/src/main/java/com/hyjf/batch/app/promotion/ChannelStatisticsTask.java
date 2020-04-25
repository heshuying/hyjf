package com.hyjf.batch.app.promotion;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.hyjf.common.util.GetDate;
import com.hyjf.mybatis.model.auto.UtmPlat;
import com.hyjf.mybatis.model.customize.app.AppChannelStatisticsCustomize;

/**
 * app推广统计定时任务
 * 
 * @author Michael
 */
public class ChannelStatisticsTask {

	@Autowired
	private ChannelStatisticsService channelStatisticsService;

	/** 运行状态 */
	private boolean isRun = true;

	public void insertStatisticsTask() {
		System.out.println("----------------APP渠道统计定时任务-------------");
		if (isRun) {
			isRun = false;
			try {
				AppChannelStatisticsCustomize channelStatisticsCustomize = new AppChannelStatisticsCustomize();
				// 当前时间 yyyy-MM-dd
				String nowDate = "2016-03-18";
				//String nowDate = GetDate.date2Str(GetDate.date_sdf);
				channelStatisticsCustomize.setTimeStartSrch(GetDate.getDayStart(nowDate));
				channelStatisticsCustomize.setTimeEndSrch(GetDate.getDayEnd(nowDate));
				// 检索所有App渠道
				List<UtmPlat> utmPlats = this.channelStatisticsService.selectUtmPlat();
				if (utmPlats != null && utmPlats.size() > 0) {
					for (int i = 0; i < utmPlats.size(); i++) {
						UtmPlat utmPlat = utmPlats.get(i);
						int sourceId = utmPlat.getSourceId();
						String sourceName = utmPlat.getSourceName();
						try {
							channelStatisticsCustomize.setSourceIdSrch(String.valueOf(utmPlat.getSourceId()));
							channelStatisticsCustomize = channelStatisticsService.selectAppChannelStatistics(channelStatisticsCustomize);
							channelStatisticsCustomize.setSourceId(String.valueOf(sourceId));
							channelStatisticsCustomize.setChannelName(sourceName);
							channelStatisticsService.insertRecord(channelStatisticsCustomize);
						} catch (Exception e) {
							e.printStackTrace();
							System.out.println("App渠道统计定时任务出错,渠道ID:" + utmPlat.getSourceId() + ",处理日期:" + nowDate);
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				isRun = true;
			}
		}
		System.out.println("----------------APP渠道统计定时任务end-------------");
	}
}
