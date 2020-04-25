package com.hyjf.batch.mgm10.blackuser;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 10月份MGM活动-筛选黑名单用户
 * 
 * @author zhangjinpeng
 * 
 */
public class Mgm10BlackUserTask {
	/** 类名 */
	private static final String THIS_CLASS = Mgm10BlackUserTask.class.getName();
	Logger _log = LoggerFactory.getLogger(Mgm10BlackUserTask.class);
	// 判断一轮定时是否跑完,如果没跑完,不允许再跑一个
	private static Boolean isOver = true;

	@Autowired
	Mgm10BlackUserService mgm10PrizeSendService;

	public void run() {
		String methodName = "run";
		if (isOver) {
			try {
				isOver = false;
				// 筛选黑名单用户
				blackUser();
			} catch (Exception e) {
				_log.info(THIS_CLASS + "==>" + methodName + "==>" + "10月份MGM活动-补偿发放虚拟奖品异常！");
			} finally {
				isOver = true;
			}
		}

	}

	/**
	 * 筛选黑名单用户
	 * 
	 * @throws Exception
	 */
	private void blackUser() throws Exception {
		String methodName = "blackUser";
		_log.info(THIS_CLASS + "==>" + methodName + "==>" + "10月份MGM活动-筛选黑名单用户开始！");
		this.mgm10PrizeSendService.updateBlackUser();
		_log.info(THIS_CLASS + "==>" + methodName + "==>" + "10月份MGM活动-筛选黑名单用户结束！");
	}

}
