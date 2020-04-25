package com.hyjf.batch.banktotal;

import java.util.List;

import com.hyjf.mybatis.model.auto.Users;

public interface BankTotalService {

	/**
	 * 查询已开户用户数量
	 * 
	 * @return
	 */
	public Integer countOpenAccountUserCount();

	/**
	 * 分页查询已开户用户的列表
	 * 
	 * @param offset
	 * @param limit
	 * @return
	 */
	public List<Users> selectOpenAccountUsers(int offset, int limit);

	/**
	 * 更新用户的账户信息
	 * 
	 * @param users
	 * @throws Exception
	 */
	public Integer updateUserAccountBankTotal(List<Users> openAccountList) throws Exception;
}
