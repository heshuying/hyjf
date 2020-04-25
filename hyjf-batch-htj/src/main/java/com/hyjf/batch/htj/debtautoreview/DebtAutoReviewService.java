package com.hyjf.batch.htj.debtautoreview;

import com.hyjf.batch.BaseService;
import com.hyjf.batch.htj.debtautoreview.enums.BorrowSendTypeEnum;

/**
 * 
 * @author 孙亮
 * @since 2015年12月24日 下午12:54:38
 */
public interface DebtAutoReviewService extends BaseService {
	/**
	 * 查询出到期但是未满标的标的,给其发短信
	 * 
	 * @param afterTime
	 * @throws Exception
	 */
	public void sendMsgToNotFullBorrow() throws Exception;
	/**
	 * 根据BorrowSendType获取AfterTime
	 * 
	 * @param BorrowSendType
	 * @return
	 * @throws Exception
	 */
	public Integer getAfterTime(BorrowSendTypeEnum BorrowSendType) throws Exception;

	/**
	 * 复审
	 * 
	 * @param afterTime
	 * @throws Exception
	 */
	public void updateDebtBorrow(Integer afterTime) throws Exception;
}
