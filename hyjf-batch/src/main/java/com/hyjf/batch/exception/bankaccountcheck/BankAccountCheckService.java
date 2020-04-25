package com.hyjf.batch.exception.bankaccountcheck;

import com.hyjf.batch.BaseService;

/**
 * 银行对账自动任务接口
 * @author cwyang
 *
 */
public interface BankAccountCheckService extends BaseService{

	/**
	 * 开始银行对账操作
	 */
	public void updateAccountCheck();

}
