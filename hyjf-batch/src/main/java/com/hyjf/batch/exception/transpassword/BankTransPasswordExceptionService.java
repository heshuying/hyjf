package com.hyjf.batch.exception.transpassword;

import com.hyjf.batch.BaseService;

/**
 * 设置交易密码掉单处理
 * @author cwyang
 * 2017-5-11
 *
 */
public interface BankTransPasswordExceptionService extends BaseService{

	/**
	 * 根据从接口更新的设置交易密码状态更新到本地库表
	 */
	public void insertIsSetPassword();
}
