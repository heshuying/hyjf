package com.hyjf.admin.finance.withdraw;

import java.util.List;
import java.util.Map;

import com.hyjf.admin.BaseService;
import com.hyjf.mybatis.model.auto.Account;
import com.hyjf.mybatis.model.auto.Accountwithdraw;
import com.hyjf.mybatis.model.customize.WithdrawCustomize;

public interface WithdrawService extends BaseService {

	/**
	 * 获取提现列表数量
	 *
	 * @param form
	 * @return
	 */
	public int getWithdrawRecordCount(WithdrawBean form);

	/**
	 * 获取提现列表
	 *
	 * @param form
	 * @return
	 */
	public List<WithdrawCustomize> getWithdrawRecordList(WithdrawBean form);

	/**
	 * 根据用户Id查询用户账户信息
	 * 
	 * @param userId
	 * @return
	 */
	public Account getAccountByUserId(Integer userId);

	/**
	 * 根据订单号查询提现订单
	 * 
	 * @param nid
	 * @return
	 */
	public Accountwithdraw queryAccountwithdrawByNid(String nid, Integer userId);

	/**
	 * 提现成功后,更新用户账户信息,提现记录
	 * 
	 * @param userId
	 * @param nid
	 * @return
	 */
	public boolean updateAccountAfterWithdraw(Integer userId, String nid, Map<String, String> param);

	/**
	 * 提现失败后,更新用户的提现记录
	 * 
	 * @param userId
	 * @param nid
	 * @return
	 */
	public boolean updateAccountAfterWithdrawFail(Integer userId, String nid) throws Exception;

}
