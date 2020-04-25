package com.hyjf.batch.hjh.borrow.ordermatch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 计划订单匹配时间
 * 
 * @author wxh
 */
public class PlanOrdeeMatchingPeriodTask {

	/** 运行状态 */
	private static int isRun = 0;

	@Autowired
	private PlanOrdeeMatchingPeriodService planOrdeeMatchingPeriodService;

	Logger _log = LoggerFactory.getLogger(PlanOrdeeMatchingPeriodTask.class);

	public void run() {
		autoReview();
	}
	private boolean autoReview() {
		if (isRun == 0) {
			isRun = 1;
			try {
				// 计划订单进入匹配期时间超过2天给工作人员发送预警邮件
				_log.info("=======计划订单进入匹配期时间超过2天给工作人员发送预警短信----开始");
				planOrdeeMatchingPeriodService.sendMsgToNotFullBorrow();
				 _log.info("=======计划订单进入匹配期时间超过2天给工作人员发送预警短信----发送成功 结束");
			} catch (Exception e) {
				_log.error("=======计划订单进入匹配期时间超过2天给工作人员发送预警短信----发送失败!" + e.getMessage());
			}finally {
                isRun = 0;
            }
		}
		return true;
	}
}
