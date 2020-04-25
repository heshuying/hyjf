package com.hyjf.admin.exception.tendercancelexception;

import java.util.List;

import com.hyjf.admin.BaseService;
import com.hyjf.mybatis.model.auto.BorrowTenderTmp;
import com.hyjf.pay.lib.bank.bean.BankCallBean;

public interface TenderCancelExceptionService extends BaseService {
	

	/**
	 * 总数COUNT
	 * 
	 * @param record
	 * @return
	 * @author Administrator
	 */
	public Integer queryCount(TenderCancelExceptionBean record);

	/**
	 * 记录
	 * 
	 * @param record
	 * @return
	 * @author Administrator
	 */
	public List<BorrowTenderTmp> queryRecordList(TenderCancelExceptionBean record);

	/***
	 * 查询相应的出借记录是否存在
	 * 
	 * @param id
	 * @return
	 */
	public boolean selectTenderIsExists(String orderId);

	/**
	 * 撤销用户的出借记录
	 * @param status 
	 * 
	 * @param orderId
	 * @return
	 * @throws Exception 
	 */
	public BankCallBean bidCancel(Integer userId, String accountId, String productId, String orgOrderId, String txAmount);

	/**
	 * 删除相应的用户出借记录
	 * 
	 * @param orderId
	 * @return
	 * @throws Exception 
	 */
	public boolean updateBidCancelRecord(BorrowTenderTmp tenderTmp ) throws Exception;

	/**
	 * 根据出借订单号查询相应的出借日志
	 * @param orderId
	 * @return
	 */
	public BorrowTenderTmp getBorrowTenderTmp(String orderId);

	/**
	 * 修复线上异常脱敏信息
	 */
    void putMessage();
}
