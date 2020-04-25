package com.hyjf.admin.finance.accountmanage;

import java.util.List;

import com.hyjf.admin.BaseService;
import com.hyjf.mybatis.model.auto.Account;
import com.hyjf.mybatis.model.auto.AccountList;
import com.hyjf.mybatis.model.customize.AccountManageCustomize;

public interface AccountManageService extends BaseService {

	/**
	 * 账户管理 （账户数量）
	 * @param accountManageBean
	 * @return
	 */
	public Integer queryAccountCount(AccountManageCustomize accountInfoCustomize) ;

	/**
	 * 账户管理 （列表）
	 * @param accountManageBean
	 * @return
	 */
	public List<AccountManageCustomize> queryAccountInfos(AccountManageCustomize accountInfoCustomize) ;

	/**
	 * 更新账户可用余额和冻结金额
	 *
	 * @return
	 */
	public int updateAccount(Integer id, String balance, String force);
	/**
	 * 更新账户信息
	 * @param account
	 * @return
	 */
	public int updateAccountSelective(Account account);
	
	
	/**
	 * 根据userId获取账户信息
	 * @param userId
	 * @return
	 */
	public Account queryAccountInfoByUserId(Integer userId);
	/**
	 * insert account_list
	 * @param list
	 * @return
	 */
	public int insertAccountList(AccountList list);
	

}

