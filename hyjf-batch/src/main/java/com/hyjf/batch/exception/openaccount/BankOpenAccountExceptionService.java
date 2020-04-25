package com.hyjf.batch.exception.openaccount;

import com.hyjf.batch.BaseService;
/**
 * 银行开户掉单处理接口
 * @author cwyang
 *
 */
public interface BankOpenAccountExceptionService extends BaseService{

	/**
	 * 银行开户掉单处理
	 */
	public void updateBankOpen();
	
}
