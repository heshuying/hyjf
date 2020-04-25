package com.hyjf.batch.hjh.borrow.planorderexception;

/**
 * 计划订单出借异常，消息通知
 * 
 * @author wxh
 * 
 */
public interface PlanOrderExceptionTaskService {
	/**
	 * 计划订单出借异常，发送短信和邮件
	 * 
	 * @param afterTime
	 * @throws Exception
	 */
	public void sendMsgToNotFullBorrow() throws Exception;
	
}
