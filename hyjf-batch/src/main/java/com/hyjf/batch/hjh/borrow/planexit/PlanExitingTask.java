package com.hyjf.batch.hjh.borrow.planexit;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.hyjf.mybatis.model.auto.BorrowWithBLOBs;

/**
 * 汇计划自动复审任务
 * 
 * @author wangxiaohui
 */
public class PlanExitingTask {

	/** 运行状态 */
	private static int isRun = 0;

	@Autowired
	private PlanExitingTaskService planOrdeeMatchingPeriodService;

	Logger _log = LoggerFactory.getLogger(PlanExitingTask.class);
	
	public void run() {
		autoReview();
	}
	private boolean autoReview() {
		if (isRun == 0) {
			isRun = 1;
			try {
				_log.info("=======计划退出时间大约2，消息通知----开始");
				// 计划退出时间超过2天给工作人员发送预警短信
				planOrdeeMatchingPeriodService.sendMsgToNotFullBorrow();
				_log.info("=======计划退出时间大约2，消息通知----发送成功");
			} catch (Exception e) {
				_log.error("=======计划退出时间大约2，消息通知----发送失败!" + e.getMessage());
			}finally {
				isRun = 0;
			}
		}
		return true;
	}


}
