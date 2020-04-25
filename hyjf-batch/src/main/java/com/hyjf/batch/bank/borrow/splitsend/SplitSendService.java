package com.hyjf.batch.bank.borrow.splitsend;

import java.util.List;

import com.hyjf.batch.BaseService;
import com.hyjf.batch.borrow.autoReview.enums.BorrowSendTypeEnum;
import com.hyjf.mybatis.model.auto.BorrowWithBLOBs;

public interface SplitSendService extends BaseService {

	/***
	 * 获取下个标的信息
	 * 
	 * @param borrowNidMain
	 * @return
	 */
	String getNextBorrowNid(String borrowNidMain);

	/**
	 * 获取所有待发布的标的
	 * @return
	 */
	public List<BorrowWithBLOBs> queryAllSplitSend();

	/**
	 * 获取标的的待发布时间
	 * @param BorrowSendType
	 * @return
	 * @throws Exception
	 */
	Integer getAfterTime(BorrowSendTypeEnum BorrowSendType) throws Exception;

	/**
	 * 获取此标的前一个标的
	 * @param borrowPreNid
	 * @param borrowPreNidNew 
	 * @return
	 */
	public BorrowWithBLOBs getPreBorrow(String borrowPreNid, String borrowPreNidNew);

	/**
	 * 发标
	 * @param borrow
	 * @param nowTime
	 * @return
	 */
	boolean updateFireBorrow(BorrowWithBLOBs borrow, long nowTime);
}
