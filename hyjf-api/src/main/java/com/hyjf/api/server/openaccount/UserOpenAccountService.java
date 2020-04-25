package com.hyjf.api.server.openaccount;

import com.hyjf.base.service.BaseService;
import com.hyjf.mybatis.model.auto.BankCard;
import com.hyjf.mybatis.model.auto.BankOpenAccountLog;
import com.hyjf.mybatis.model.auto.Users;
import com.hyjf.pay.lib.bank.bean.BankCallBean;

/**
 * 用户开户Service
 * 
 * @author liuyang
 *
 */
public interface UserOpenAccountService extends BaseService {
	/**
	 * 根据手机号检索用户信息
	 * 
	 * @param mobile
	 * @return
	 */
	Users selectUserByMobile(String mobile);
	/**
	 * 调用银行发送短信接口
	 * 
	 * @param userId
	 * @param orderId
	 * @param srvTxCode
	 * @param mobile
	 * @param channel
	 * @return
	 */
	BankCallBean sendOpenAccountSms(Integer userId, String orderId, String srvTxCode, String mobile, String channel);

	/**
	 * 更新开户日志表
	 * 
	 * @param userId
	 * @param orderId
	 * @param srvAuthCode
	 * @return
	 */
	boolean updateUserAccountLog(Integer userId, String orderId, String srvAuthCode);


	/**
	 * 保存用户的开户参数
	 * 
	 * @param openAccountLog
	 * @param openAccoutBean
	 * @return
	 */
	boolean updateUserAccountLog(BankOpenAccountLog openAccountLog, BankCallBean openAccoutBean);

	/**
	 * 更新开户日志表
	 * 
	 * @param userId
	 * @param logOrderId
	 * @param status
	 */
	void updateUserAccountLog(Integer userId, String logOrderId, int status);

	/**
	 * 开户成功后,更新用户账户信息
	 * 
	 * @param openAccountResult
	 * @return
	 */
	boolean updateUserAccount(BankCallBean openAccountResult);

	/**
	 * 检验用户身份证号是否在汇盈存在
	 * @param idNo
	 * @return
	 */
    boolean checkIdNo(String idNo);
	/**
	 * 检索银行卡联号
	 * @param userId
	 * @return
	 */
	BankCard checkPayAllianceCode(Integer userId);

	Users selectUserByIdNo(String idNo);
	/**
	 * 
	 * 根据手机号查询银行开户电子账户号
	 * @author pcc
	 * @param mobile
	 * @return
	 */
    String getBankOpenAccountByMobile(String mobile);

	/**
	 * 判断用户是否存在电子账户
	 *
	 * @param userId
	 * @return
	 */
	boolean existBankAccountId(Integer userId);

	/**
	 * 根据用户ID 查询用户银行卡信息
	 * @return
	 */
	BankCard selectBankCardByUserId(Integer userId);
}
