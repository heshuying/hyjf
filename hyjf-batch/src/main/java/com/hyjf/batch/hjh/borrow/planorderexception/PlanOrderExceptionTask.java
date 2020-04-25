package com.hyjf.batch.hjh.borrow.planorderexception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 计划订单出借异常，消息通知
 * 
 * @author wxh
 * 
 */
public class PlanOrderExceptionTask {

	/** 运行状态 */
	private static int isRun = 0;

	@Autowired
	private PlanOrderExceptionTaskService planOrdeeMatchingPeriodService;

	Logger _log = LoggerFactory.getLogger(PlanOrderExceptionTask.class);
	
	public void run() {
		autoReview();
	}
	private boolean autoReview() {
		if (isRun == 0) {
			isRun = 1;
			try {
				_log.info("=======计划订单出借异常，消息通知----开始");
				// 计划订单出借异常，消息通知
				planOrdeeMatchingPeriodService.sendMsgToNotFullBorrow();
				 _log.info("=======计划订单出借异常，消息通知----发送成功");
			} catch (Exception e) {
				_log.error("=======计划订单出借异常，消息通知----发送失败!" + e.getMessage());
			}finally {
                isRun = 0;
            }
		}
		return true;
	}

}
