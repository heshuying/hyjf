package com.hyjf.batch.exception.banktendercancel;

import com.hyjf.batch.BaseService;

/**
 * 
 * @author cwyang
 * 优化出借异常数据
 *
 */
public interface BankTenderCancelExceptionService  extends BaseService{

	/**
	 * 优化出借异常数据
	 */
	public int updateTenderCancelData();

	public void start();

}
