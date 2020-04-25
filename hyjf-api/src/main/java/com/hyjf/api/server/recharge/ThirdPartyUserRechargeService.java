package com.hyjf.api.server.recharge;

import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.base.service.BaseService;
import com.hyjf.mybatis.model.auto.BankCard;
import com.hyjf.mybatis.model.auto.BankOpenAccount;
import com.hyjf.mybatis.model.auto.HjhInstConfig;
import com.hyjf.pay.lib.bank.bean.BankCallBean;

/**
 * 外部服务接口:用户充值Service
 * 
 * @author liuyang
 *
 */
public interface ThirdPartyUserRechargeService extends BaseService {

	/**
	 * 根据用户电子账户号检索用户开户信息
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
	 * 调用银行发送短信接口
	 * 
	 * @param userId
	 * @param cardNo
	 * @param mobile
	 * @param channel
	 * @return
	 */
	BankCallBean sendRechargeOnlineSms(Integer userId, String cardNo, String mobile, String channel);

	/**
	 * 根据用户ID,获取短信序列号
	 * 
	 * @param userId
	 * @param txcodeDirectRechargeOnline
	 * @return
	 */
	String selectBankSmsSeq(Integer userId, String txcodeDirectRechargeOnline);

	/**
	 * 插入充值记录
	 * 
	 * @param bean
	 * @return
	 */
	int insertRechargeInfo(BankCallBean bean);

	/**
	 * 冲值后,回调处理
	 * 
	 * @param bankCallBean
	 * @param params
	 * @return
	 */
	JSONObject handleRechargeInfo(BankCallBean bankCallBean, Map<String, String> params);
}
