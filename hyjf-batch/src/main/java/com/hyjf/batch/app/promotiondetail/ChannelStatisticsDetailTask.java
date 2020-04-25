package com.hyjf.batch.app.promotiondetail;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.hyjf.mybatis.model.auto.AppChannelStatisticsDetail;
import com.hyjf.mybatis.model.customize.batch.BatchPcPromotionCustomize;

/**
 * APP渠道统计明细老数据修复定时 此定时只跑一次
 * 
 * @author liuyang
 *
 */
public class ChannelStatisticsDetailTask {
	/** 运行状态 */
	private static int isRun = 0;

	@Autowired
	private ChannelStatisticsDetailService channelStatisticsDetailService;

	public void run() {
		// 调用更新接口
		update();
	}

	private void update() {
		System.out.println("APP渠道统计明细老数据修复定时 start");
		if (isRun == 0) {
			isRun = 1;
			try {
				// 检索APP渠道统计明细老数据
				List<AppChannelStatisticsDetail> appChannelStatisticsDetailList = this.channelStatisticsDetailService.selectChannelStatisticsDetailList();
				if (appChannelStatisticsDetailList != null && appChannelStatisticsDetailList.size() > 0) {
					for (AppChannelStatisticsDetail appChannelStatisticsDetail : appChannelStatisticsDetailList) {
						Integer userId = appChannelStatisticsDetail.getUserId();
						BatchPcPromotionCustomize batchAppPromotionCustomize = this.channelStatisticsDetailService.selectAppUserInvestByUserId(userId);
						if (batchAppPromotionCustomize != null) {
							// 更新hyjf_app_channel_statistics_detail表
							boolean isupdateFlag = this.channelStatisticsDetailService.updateAppChannelStatisticsDetail(appChannelStatisticsDetail, batchAppPromotionCustomize);
							if (!isupdateFlag) {
								throw new Exception("更新hyjf_app_channel_statistics_detail表失败!");
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
		System.out.println("APP渠道统计明细老数据修复定时 end");
	}
}
