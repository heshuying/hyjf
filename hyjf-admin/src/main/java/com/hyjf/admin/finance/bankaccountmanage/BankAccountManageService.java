package com.hyjf.admin.finance.bankaccountmanage;

import java.math.BigDecimal;
import java.util.List;

import com.hyjf.admin.BaseService;
import com.hyjf.mybatis.model.auto.Account;
import com.hyjf.mybatis.model.auto.AccountList;
import com.hyjf.mybatis.model.customize.BankAccountManageCustomize;

public interface BankAccountManageService extends BaseService {

	/**
	 * 账户管理 （账户数量）
	 * 
	 * @param accountManageBean
	 * @return
	 */
	public Integer queryAccountCount(BankAccountManageCustomize accountInfoCustomize);

	/**
	 * 账户管理 （列表）
	 * 
	 * @param accountManageBean
	 * @return
	 */
	public List<BankAccountManageCustomize> queryAccountInfos(BankAccountManageCustomize accountInfoCustomize);

	/**
	 * 更新账户可用余额和冻结金额
	 *
	 * @return
	 */
	public int updateAccount(Integer id, BigDecimal balance, BigDecimal frost);

	/**
	 * 更新账户信息
	 * 
	 * @param account
	 * @return
	 */
	public int updateAccountSelective(Account account);

	/**
	 * 根据userId获取账户信息
	 * 
	 * @param userId
	 * @return
	 */
	public Account queryAccountInfoByUserId(Integer userId);

	/**
	 * insert account_list
	 * 
	 * @param list
	 * @return
	 */
	public int insertAccountList(AccountList list);
	
	public String updateAccountCheck(Integer userId, String startTime, String endTime); 

}
