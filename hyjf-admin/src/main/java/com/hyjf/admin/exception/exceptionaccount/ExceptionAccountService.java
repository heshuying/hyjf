package com.hyjf.admin.exception.exceptionaccount;

import java.util.List;

import com.hyjf.mybatis.model.auto.ExceptionAccount;

public interface ExceptionAccountService {
	/**
	 * 获取总记录条数
	 * 
	 * @param param
	 * @return
	 */
	public int countRecordTotal(String userName,Long customId,String mobile);

	/**
	 * 获取所有注册记录
	 * 
	 * @param param
	 * @param limitStart
	 * @param limitEnd
	 * @return
	 */
	public List<ExceptionAccount> searchRecord(String userName,Long customId,String mobile,int limitStart, int limitEnd);
	
	/**
	 * 同步账户
	 * @param customId
	 */
	public void syncAccount(Integer id);
}
