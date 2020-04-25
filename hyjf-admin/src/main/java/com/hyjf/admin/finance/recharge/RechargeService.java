package com.hyjf.admin.finance.recharge;

import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.admin.BaseService;
import com.hyjf.mybatis.model.auto.Account;
import com.hyjf.mybatis.model.auto.AccountRecharge;
import com.hyjf.mybatis.model.auto.BanksConfig;
import com.hyjf.mybatis.model.auto.Users;
import com.hyjf.mybatis.model.customize.RechargeCustomize;
import com.hyjf.mybatis.model.customize.UserInfoCustomize;
import com.hyjf.pay.lib.bank.bean.BankCallBean;

public interface RechargeService extends BaseService {

	/**
	 * 充值管理 （账户数量）
	 * 
	 * @param accountManageBean
	 * @return
	 */
	public Integer queryRechargeCount(RechargeCustomize rechargeCustomize);

	/**
	 * 充值管理 （列表）
	 * 
	 * @param accountManageBean
	 * @return
	 */
	public List<RechargeCustomize> queryRechargeList(RechargeCustomize rechargeCustomize);

	/**
	 * 根据订单号nid获取充值信息
	 * 
	 * @param nid
	 * @return
	 */
	public AccountRecharge queryRechargeByNid(String nid);

	/**
	 * 手动充值处理
	 * 
	 * @param form
	 * @param chinapnrBean
	 * @return
	 */
	public int updateHandRechargeRecord(RechargeBean form, BankCallBean bankcallBean, UserInfoCustomize userInfoCustomize, String accountId);

	/**
	 * 根据用户名查询用户ID
	 * 
	 * @param username
	 * @return
	 */
	public Users queryUserInfoByUserName(String username);

	/**
	 * 根据用户名查询用户信息
	 * 
	 * @param username
	 * @return
	 */
	public UserInfoCustomize queryUserInfoByName(String username);

	/**
	 * 根据用户名校验用户条件 1. 校验用户是否已经存在 2. 校验用户是否已经开户
	 * 
	 * @param outUserName
	 * @param ret
	 */
	public void checkTransfer(String outUserName, JSONObject ret);

	/**
	 * 充值掉单后,更新用户的账户信息
	 * 
	 * @param userId
	 * @param nid
	 * @return
	 */
	public boolean updateAccountAfterRecharge(Integer userId, String nid) throws Exception;

	/**
	 * 获取银行列表
	 * 
	 * @return
	 */
	public List<BanksConfig> getBankcardList();

	/**
	 * 根据用户Id查询用户账户信息
	 * 
	 * @param userId
	 * @return
	 */
	public Account getAccountByUserId(Integer userId);

	/**
	 * 充值失败后,更新用户订单状态
	 * 
	 * @param userId
	 * @param nid
	 * @return
	 */
	public boolean updateAccountAfterRechargeFail(Integer userId, String nid) throws Exception;

	/**
	 * 更新充值状态
	 * 
	 * @param userId
	 * @param nid
	 * @return
	 */
	public boolean updateRechargeStatus(Integer userId, String nid) throws Exception;
}
