package com.hyjf.batch.invest;

import java.util.List;

import com.hyjf.mybatis.model.auto.Users;

/**
 * 债权迁移后,计算用户的累计出借
 * 
 * @author liuyang
 *
 */
public interface BankInvestSumService {

	/**
	 * 检索已在汇付开户的用户总数
	 * 
	 * @return
	 */
	public Integer countOpenAccountUsers();

	/**
	 * 分页查询已开户的用户
	 * 
	 * @param limitStart
	 * @param limitEnd
	 * @return
	 */
	public List<Users> selectOpenAccountUsers(int limitStart, int limitEnd);

	/**
	 * 更新用户的累计出借
	 * 
	 * @param openAccountList
	 */
	public Integer updateBankInvestSum(List<Users> openAccountList);
}
