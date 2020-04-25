package com.hyjf.batch.hjh.borrow.planexit;

import java.util.List;

import com.hyjf.batch.BaseService;
import com.hyjf.batch.borrow.autoReview.enums.BorrowSendTypeEnum;
import com.hyjf.mybatis.model.auto.BorrowWithBLOBs;

/**
 * 
 * @author wangxiaohui
 */
public interface PlanExitingTaskService {
	/**
	 * 计划推出中时间超时通知
	 * 
	 * @param afterTime
	 * @throws Exception
	 */
	public void sendMsgToNotFullBorrow() throws Exception;
	
}
