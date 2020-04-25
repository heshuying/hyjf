package com.hyjf.batch.mgm10.prizesend;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 10月份MGM活动-补偿发放虚拟奖品（优惠券）
 * 
 * @author zhangjinpeng
 * 
 */
public class Mgm10PrizeSendTask {
	/** 类名 */
	private static final String THIS_CLASS = Mgm10PrizeSendTask.class.getName();
	Logger _log = LoggerFactory.getLogger(Mgm10PrizeSendTask.class);
	// 判断一轮定时是否跑完,如果没跑完,不允许再跑一个
	private static Boolean isOver = true;

	@Autowired
	Mgm10PrizeSendService mgm10PrizeSendService;

	public void run() {
		String methodName = "run";
		if (isOver) {
			try {
				isOver = false;
				// 发放虚拟奖品
				prizeSend();
			}catch(Exception e){
				_log.info(THIS_CLASS + "==>" + methodName + "==>" + "10月份MGM活动-补偿发放虚拟奖品异常！");
			} finally {
				isOver = true;
			}
		}

	}

	/**
	 * 补偿发放虚拟奖品
	 * @throws Exception 
	 */
	private void prizeSend() throws Exception {
		String methodName = "prizeSend";
		_log.info(THIS_CLASS + "==>" + methodName + "==>" + "10月份MGM活动-补偿发放虚拟奖品开始！");
		mgm10PrizeSendService.updatePrizeSend();
		_log.info(THIS_CLASS + "==>" + methodName + "==>" + "10月份MGM活动-补偿发放虚拟奖品结束！");
	}

}
