package com.hyjf.batch.prize.reset;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 奖品数量重置
 * 
 * @author zhangjinpeng
 * 
 */
public class PrizeResetTask {
	/** 类名 */
	private static final String THIS_CLASS = PrizeResetTask.class.getName();
	Logger _log = LoggerFactory.getLogger(PrizeResetTask.class);
	// 判断一轮定时是否跑完,如果没跑完,不允许再跑一个
	private static Boolean isOver = true;

	@Autowired
	PrizeResetService prizeResetService;

	public void run() {
		if (isOver) {
			try {
				isOver = false;
				// 奖品重置
				prizeReset();
			} finally {
				isOver = true;
			}
		}
	}

	/**
	 * 奖品重置
	 */
	private void prizeReset() {
		String methodName = "prizeReset";
		_log.info(THIS_CLASS + "==>" + methodName + "==>" + "活动期内邀请用户注册获得推荐星开始！");
		// 奖品重置
		prizeResetService.updatePrizeReset();

		_log.info(THIS_CLASS + "==>" + methodName + "==>" + "活动期内邀请用户注册获得推荐星结束！");
	}

}
