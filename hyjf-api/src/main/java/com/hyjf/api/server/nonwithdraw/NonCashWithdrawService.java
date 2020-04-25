package com.hyjf.api.server.nonwithdraw;

import java.math.BigDecimal;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.base.service.BaseService;
import com.hyjf.mybatis.model.auto.Accountwithdraw;
import com.hyjf.mybatis.model.auto.BankCard;
import com.hyjf.mybatis.model.auto.BankOpenAccount;
import com.hyjf.mybatis.model.auto.CorpOpenAccountRecord;
import com.hyjf.mybatis.model.auto.HjhUserAuth;
import com.hyjf.pay.lib.bank.bean.BankCallBean;

/**
 * 外部服务接口:用户提现Service
 * 
 * @author sunss
 *
 */
public interface NonCashWithdrawService extends BaseService {

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
	 * 根据userid查询用户免密授权信息
	 * @author sunss
	 * @param userId
	 * @return 
	 */
    HjhUserAuth getUserAuthByUserId(Integer userId);

    /**
     * 查询手续费
     * @param instCode
     * @return
     */
	BigDecimal getUserFee(String instCode);
}
