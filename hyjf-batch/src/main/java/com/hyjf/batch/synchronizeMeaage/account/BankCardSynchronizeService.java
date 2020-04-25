package com.hyjf.batch.synchronizeMeaage.account;

import com.hyjf.mybatis.model.auto.AccountMobileAynch;

/**
 * Service类
 * 
 * @author lisheng
 *
 */
public interface BankCardSynchronizeService {

	/**
	 * 更新银行卡信息
	 * 
	 * @param accountMobileAynch
	 * @return 用户的身份证号
	 */
	public boolean updateAccountBankByUserId(AccountMobileAynch accountMobileAynch);



}
