package com.hyjf.batch.interestsum;

import java.util.List;

import com.hyjf.mybatis.model.auto.Users;

public interface InterestSumService {

	/**
	 * 检索已在汇付开户的用户总数
	 * 
	 * @return
	 */
	public Integer countOpenAccountUsers();

	/**
	 * 获取已开户用户列表
	 * 
	 * @param offset
	 * @param limit
	 * @return
	 */
	public List<Users> selectOpenAccountUsers(int offset, int limit);

	/**
	 * 更新用户的累计收益
	 * 
	 * @param openAccountList
	 * @return
	 */
	public Integer updateInterestSum(List<Users> openAccountList)  throws Exception;
}
