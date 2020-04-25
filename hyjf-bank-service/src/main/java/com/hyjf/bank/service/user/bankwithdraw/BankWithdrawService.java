/**
 * Description:用户充值
 * Copyright: Copyright (HYJF Corporation)2015
 * Company: HYJF Corporation
 * @author: GOGTZ-T
 * @version: 1.0
 * Created at: 2015年12月4日 下午1:45:13
 * Modification History:
 * Modified by :
 */
package com.hyjf.bank.service.user.bankwithdraw;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.bank.service.BaseService;
import com.hyjf.bank.service.sensorsdata.SensorsDataBean;
import com.hyjf.common.security.utils.MD5;
import com.hyjf.common.util.GetDate;
import com.hyjf.mybatis.model.auto.Accountwithdraw;
import com.hyjf.mybatis.model.auto.BankCard;
import com.hyjf.mybatis.model.auto.BankConfig;
import com.hyjf.mybatis.model.auto.BanksConfig;
import com.hyjf.mybatis.model.auto.CorpOpenAccountRecord;
import com.hyjf.mybatis.model.auto.SmsCode;
import com.hyjf.mybatis.model.auto.SmsCodeExample;
import com.hyjf.pay.lib.bank.bean.BankCallBean;

public interface BankWithdrawService extends BaseService {

	/**
	 * 根据用户ID取得该用户的提现卡
	 *
	 * @param userId
	 * @return
	 */
	public List<BankCard> getBankCardByUserId(Integer userId);

	/**
	 * 根据code取得银行信息
	 *
	 * @param userId
	 * @return
	 */
	public BankConfig getBankInfo(String code);

	/**
	 * 用户提现前处理
	 *
	 * @param bean
	 * @return
	 */
	public int updateBeforeCash(BankCallBean bean, Map<String, String> params);

	/**
	 * 用户提现后处理
	 *
	 * @param bean
	 * @return
	 */
	public JSONObject handlerAfterCash(BankCallBean bean, Map<String, String> params) throws Exception;

	/**
	 * 获取用户的身份证号
	 *
	 * @param userId
	 * @return 用户的身份证号
	 */
	public String getUserIdcard(Integer userId);

	/**
	 * 判断是否提现成功
	 *
	 * @param ordId
	 * @return
	 */
	public Accountwithdraw getAccountWithdrawByOrdId(String ordId);

	/**
	 * 更新提现表
	 *
	 * @param ordId
	 * @return
	 */
	public int updateAccountWithdrawByOrdId(String ordId, String reason);

	/**
	 * 获取用户的银行卡信息
	 *
	 * @param userId
	 * @return 用户的银行卡信息
	 */
	public BankCard getBankInfo(Integer userId, String bankId);

	/**
	 * 获取用户的银行卡费率
	 *
	 * @param userId
	 * @return 用户的银行卡费率
	 */
	public String getWithdrawFee(Integer userId, String bankId);

	/**
	 * 判断是否提现成功
	 *
	 * @param bean
	 * @return S:成功 F:失败
	 */
	public String checkCashResult(BankCallBean bean);

	/**
	 * 取得成功信息
	 *
	 * @param ordId
	 * @return
	 */
	public JSONObject getMsgData(String ordId);

	/**
	 * 拼装返回信息
	 * 
	 * @param errorDesc
	 * @param status
	 * @return
	 */
	public JSONObject jsonMessage(String errorDesc, String error);

	/**
	 * 根据用户ID查询企业用户信息
	 * 
	 * @param userId
	 * @return
	 */
	public CorpOpenAccountRecord getCorpOpenAccountRecord(Integer userId);

	int updateCheckMobileCode(String mobile, String verificationCode, String verificationType, String platform, Integer searchStatus, Integer updateStatus);

	/**
	 * 发送神策数据统计MQ
	 *
	 * @param sensorsDataBean
	 */
	void sendSensorsDataMQ(SensorsDataBean sensorsDataBean);
}
