package com.hyjf.batch.activity.activity;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.hyjf.common.util.GetDate;
import com.hyjf.mybatis.model.auto.Ads;

/**
 * 进行中的活动列表状态变更定时
 * 
 * @author liuyang
 *
 */
public class ActivityEndTask {

	// 判断一轮定时是否跑完,如果没跑完,不允许再跑一个
	private static Boolean isOver = true;

	@Autowired
	private ActivityEndService activityEndService;

	public void run() throws Exception {
		if (isOver) {
			try {
				isOver = false;
				updateActivityEndStatus();
			} finally {
				isOver = true;
			}
		}
	}

	/**
	 * 更新进行中活动状态
	 * 
	 * @throws Exception
	 */
	private void updateActivityEndStatus() throws Exception {
		System.out.println("-------------------进行中活动结束状态变更定时开始--------------------");
		// 检索进行中活动列表
		List<Ads> activityList = this.activityEndService.selectActivityList();

		if (activityList != null && activityList.size() > 0) {
			for (Ads ads : activityList) {
				// 取得活动结束时间
				Integer endTime = GetDate.dateString2Timestamp(ads.getEndTime());
				// 活动时间小于当前时间
				if (endTime <= GetDate.getMyTimeInMillis()) {
					// 将活动结束状态更新为:已结束
					ads.setIsEnd(1);
					boolean isUpdateFlag = this.activityEndService.updateActivityEndStatus(ads);
					if (!isUpdateFlag) {
						throw new Exception("更新活动结束状态失败!");
					}
				}
			}
		}
		System.out.println("-------------------进行中活动结束状态变更定时结束--------------------");
	}

}
