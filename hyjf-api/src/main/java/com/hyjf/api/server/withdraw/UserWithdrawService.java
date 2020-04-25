package com.hyjf.api.server.withdraw;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.base.service.BaseService;
import com.hyjf.mybatis.model.auto.*;
import com.hyjf.mybatis.model.customize.thirdparty.UserWithdrawRecordCustomize;
import com.hyjf.pay.lib.bank.bean.BankCallBean;

/**
 * 外部服务接口:用户提现Service
 * 
 * @author liuyang
 *
 */
public interface UserWithdrawService extends BaseService {

	/**
	 * 根据电子账户号查询用户开户信息
	 * 
	 * @param accountId
	 * @return
	 */
	BankOpenAccount selectBankOpenAccountByAccountId(String accountId);

	/**
	 * 根据用户ID查询用户银行卡信息
	 * 
	 * @param userId
	 * @return
	 */
	BankCard getBankCardByUserId(Integer userId);

	/**
	 * 获取用户的银行卡费率
	 *
	 * @param userId
	 * @return 用户的银行卡费率
	 */
	String getWithdrawFee(Integer userId, String bankId, BigDecimal amount);

	/**
	 * 获取用户的银行卡信息
	 *
	 * @param userId
	 * @return 用户的银行卡信息
	 */
	BankCard getBankInfo(Integer userId, String bankId);

	/**
	 * 根据用户ID查询用户是否是企业用户
	 * 
	 * @param userId
	 * @return
	 */
	CorpOpenAccountRecord getCorpOpenAccountRecord(Integer userId);

	/**
	 * 提现前,插入提现记录表
	 * 
	 * @param bean
	 * @param params
	 * @return
	 */
	int updateBeforeCash(BankCallBean bean, Map<String, String> params);

	/**
	 * 根据提现订单号查询提现记录
	 * 
	 * @param logOrderId
	 * @return
	 */
	Accountwithdraw getAccountWithdrawByOrdId(String logOrderId);

	/**
	 * 用户提现后处理
	 *
	 * @param bean
	 * @return
	 */
	JSONObject handlerAfterCash(BankCallBean bean, Map<String, String> params) throws Exception;

	/**
	 * 拼装返回信息
	 * 
	 * @param errorDesc
	 * @param status
	 * @return
	 */
	JSONObject jsonMessage(String errorDesc, String error);

	/**
	 * 
	 * 获取配置机构的提现手续费
	 * @author sunss
	 * @param instCode
	 * @return
	 */
    String getUserFee(String instCode);

    /**
     * 
     * 获取第三方查询提现信息列表
     * @author pcc
     * @param param
     * @return
     */
    List<UserWithdrawRecordCustomize> getThirdPartyUserWithdrawRecord(Map<String, Object> param);

	/**
	 * 根据用户Id查询投资记录数
	 *
	 * @param userId
	 * @return
	 */
	int getTenderRecord(Integer userId);

	/**
	 * 根据用户Id查询充值金额
	 *
	 * @param userId
	 * @return
	 */
	List<AccountRecharge> getRechargeMoney(Integer userId);

}
