package com.hyjf.batch.managefee;

import java.util.List;

import com.hyjf.mybatis.model.auto.Users;

/**
 * 债权迁移后,更新借款人账户信息
 * 
 * @author liuyang
 *
 */
public interface ManageFeeService {
	/**
	 * 检索借款人列表
	 * 
	 * @return
	 */
	public List<Users> searchBorrowUsersList();

	/**
	 * 更新借款人账户信息
	 * 
	 * @param users
	 */
	public void updateBorrowUserAccount(Users users) throws Exception;
}
