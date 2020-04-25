package com.hyjf.batch.bank.borrow.autoreview;

import java.util.List;

import com.hyjf.batch.BaseService;
import com.hyjf.batch.borrow.autoReview.enums.BorrowSendTypeEnum;
import com.hyjf.mybatis.model.auto.BorrowWithBLOBs;

/**
 * 
 * @author 孙亮
 * @since 2015年12月24日 下午12:54:38
 */
public interface BatchAutoReviewService extends BaseService {
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
	 * @param borrow 
	 * 
	 * @param afterTime
	 * @throws Exception
	 */
	public void updateBorrow(BorrowWithBLOBs borrow, Integer afterTime) throws Exception;
	
	/**
	 * 查询相应的待复审的标的
	 * @return
	 */
	public List<BorrowWithBLOBs> selectAutoReview();
}
