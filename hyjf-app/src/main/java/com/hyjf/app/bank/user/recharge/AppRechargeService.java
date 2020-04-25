/**
 * Description:用户充值
 * Copyright: Copyright (HYJF Corporation)2015
 * Company: HYJF Corporation
 * @author: GOGTZ-T
 * @version: 1.0
 *           Created at: 2015年12月4日 下午1:45:13
 *           Modification History:
 *           Modified by :
 */
package com.hyjf.app.bank.user.recharge;

import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.app.BaseService;
import com.hyjf.mybatis.model.auto.AccountRecharge;
import com.hyjf.mybatis.model.auto.BankCard;
import com.hyjf.mybatis.model.auto.CorpOpenAccountRecord;
import com.hyjf.pay.lib.bank.bean.BankCallBean;

public interface AppRechargeService extends BaseService {
	/**
	 * 根据用户Id,银行卡号检索用户银行卡信息
	 * 
	 * @param userId
	 * @param cardNo
	 * @return
	 */
	public BankCard selectBankCardByUserId(Integer userId);

	/**
	 * 根据用户Id检索用户的身份证号
	 * 
	 * @param userId
	 * @return
	 */
	public String getUserIdcard(Integer userId);

	/**
	 * 插入充值记录
	 * 
	 * @param bean
	 * @param params
	 * @return
	 */
	public int insertRechargeInfo(BankCallBean bean);

	/**
	 * 冲值后,回调处理
	 * 
	 * @param bean
	 * @param params
	 * @return
	 */
	public JSONObject handleRechargeInfo(BankCallBean bean, Map<String, String> params);

	/**
	 * 根据用户Id查询企业用户的账户信息
	 * @param userId
	 * @return
	 */
	public CorpOpenAccountRecord getCorpOpenAccountRecord(Integer userId);
	
	/**
	 * 发送验证码
	 *
	 * @param request
	 * @param form
	 * @return Map<String, Object> {success: 1,message:调用验证码接口成功,srvAuthCode:
	 *         srvAuthCode}
	 */
	public BankCallBean sendRechargeOnlineSms(Integer userId, String cardNo, String mobile, String client);
	
	/**
	 * 初始化充值信息
	 * @param bean
	 * @return
	 */
	public int insertRechargeOnlineInfo(BankCallBean bean);
	
	/**
	 * 短信充值处理
	 * 
	 * @param bean
	 * @param params
	 * @return
	 */
	public JSONObject handleRechargeOnlineInfo(BankCallBean bean, Map<String, String> params);

	/**
	 * 查询充值是否成功
	 * @param userId
	 * @param nid
	 * @return
	 */
	AccountRecharge selectRechargeInfo(int userId, String nid);

	/**
	 * 查询用户的短信序列号
	 * 
	 * @param userId
	 * @param srvTxCode
	 * @param srvAuthCode
	 * @return
	 */
	public String selectBankSmsSeq(Integer userId, String srvTxCode);

}
