package com.hyjf.batch.recommend.send;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 发放推荐星
 * 
 * @author zhangjinpeng
 * 
 */
public class RecommendSendTask {
	/** 类名 */
	private static final String THIS_CLASS = RecommendSendTask.class.getName();
	Logger _log = LoggerFactory.getLogger(RecommendSendTask.class);
	// 判断一轮定时是否跑完,如果没跑完,不允许再跑一个
	private static Boolean isOver = true;

	@Autowired
	RecommendSendService prizeResetService;

	public void run() {
		String methodName = "run";
		if (isOver) {
			try {
				isOver = false;
				// 发放推荐星
				recommendSend();
			}catch(Exception e){
				_log.info(THIS_CLASS + "==>" + methodName + "==>" + "发放推荐星异常！");
			} finally {
				isOver = true;
			}
		}

	}

	/**
	 * 发放推荐星
	 * @throws Exception 
	 */
	private void recommendSend() throws Exception {
		String methodName = "recommendSend";
		_log.info(THIS_CLASS + "==>" + methodName + "==>" + "发放推荐星开始！");
		// 注册开户发放1颗推荐星
		prizeResetService.updateOpenAccountSend();
		// 有效邀请发放2颗推荐星
		prizeResetService.updateTenderSend();
		// 3次有效邀请发放1颗推荐星
		prizeResetService.updateThrInviteSend();

		_log.info(THIS_CLASS + "==>" + methodName + "==>" + "发放推荐星结束！");
	}

}
