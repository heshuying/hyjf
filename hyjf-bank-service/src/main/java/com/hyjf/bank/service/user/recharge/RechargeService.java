package com.hyjf.bank.service.user.recharge;

import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.bank.service.BaseService;
import com.hyjf.bank.service.sensorsdata.SensorsDataBean;
import com.hyjf.mybatis.model.auto.*;
import com.hyjf.pay.lib.bank.bean.BankCallBean;

/**
 * 用户充值Service
 * 
 * @author liuyang
 *
 */
public interface RechargeService extends BaseService {
	/**
	 * 根据用户Id,银行卡号检索用户银行卡信息
	 * 
	 * @param userId
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
	 * 根据用户ID查询企业用户信息
	 * 
	 * @param userId
	 * @return
	 */
	public CorpOpenAccountRecord getCorpOpenAccountRecord(Integer userId);
	
	/**
	 * 发送验证码
	 *
	 * @param client
	 * @param mobile
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
	 * 查询用户的短信序列号
	 * 
	 * @param userId
	 * @param srvTxCode
	 * @return
	 */
	public String selectBankSmsSeq(Integer userId, String srvTxCode);

	/**
	 * 查询充值是否成功
	 * @param userId
	 * @param nid
	 * @return
	 */
	AccountRecharge selectRechargeInfo(int userId, String nid);
	/**
	 * 获取24小时内充值记录
	 * @return 充值记录
	 * @author lisheng
	 */
	public List<AccountRecharge> getTodayRecharge(Integer userId);

	/**
	 * 获取用户出借记录
	 * @return 用户出借记录
	 * @author lisheng
	 */
	public int getBorrowTender(Integer userId);

	/**
	 * 获取用户角色
	 * @return 用户角色
	 * @author lisheng
	 */
	public String getUserRoId(Integer userId);

	/**
	 * 判断江西银行绑卡使用新/旧接口
	 *
	 * @return 0旧接口/1新接口
	 */
	public Integer getBandCardBindUrlType(String interfaceType);

	/**
	 * 发送神策数据统计
	 *
	 * @param sensorsDataBean
	 */
	void sendSensorsDataMQ(SensorsDataBean sensorsDataBean);

}
