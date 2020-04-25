package com.hyjf.admin.exception.freezexception;

import java.util.List;

import com.hyjf.mybatis.model.auto.FreezeHistory;

public interface FreezExceptionService {
	/**
	 * 总数COUNT
	 * 
	 * @param record
	 * @return
	 * @author Administrator
	 */
	public Integer queryCount(FreezExceptionBean record);

	/**
	 * 记录
	 * 
	 * @param record
	 * @return
	 * @author Administrator
	 */
	public List<FreezeHistory> queryRecordList(FreezExceptionBean record);

	/**
	 * 解冻订单号在本库中是否存在
	 * 
	 * @param trxId
	 * @return
	 */
	public boolean selsectTrxIdIsExists(String trxId);

	/**
	 * 解冻
	 * 
	 * @param record
	 */
	public String updateFreezRecord(FreezExceptionBean record);

}
