package com.hyjf.batch.exception.account;

import com.hyjf.batch.BaseService;

public interface ExceptionAccountService extends BaseService {
	/**
	 * 同步汇付账户数据
	 */
	public void syncHuiFuAccounts();

}
