package com.hyjf.batch.hjh.borrow.ordermatch;

/**
 * 计划订单匹配时间
 *
 * @author wxh
 */
public interface PlanOrdeeMatchingPeriodService  {
	/**
	 * 计划订单进入匹配期时间超过2天给工作人员发送预警短信
	 *
	 * @throws Exception
	 */
	public void sendMsgToNotFullBorrow() throws Exception;
	
}
