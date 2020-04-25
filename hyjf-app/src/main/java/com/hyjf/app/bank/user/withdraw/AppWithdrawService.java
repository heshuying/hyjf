package com.hyjf.app.bank.user.withdraw;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.app.BaseService;
import com.hyjf.mybatis.model.auto.*;
import com.hyjf.pay.lib.bank.bean.BankCallBean;

/**
 * app提现Serivce类
 * 
 * @author liuyang
 */
public interface AppWithdrawService extends BaseService {

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
	 * @param code
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
	public String getWithdrawFee(Integer userId, String cardNo);

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
	 * @param error
	 * @return
	 */
	public JSONObject jsonMessage(String errorDesc, String error);

	/**
	 * 获取汇付天下的银行卡列表
	 * 
	 * @param userId
	 * @return
	 */
	public List<AccountBank> getBankHfCardByUserId(Integer userId);

	/**
	 * 根据用户Id查询企业用户信息
	 * 
	 * @param userId
	 * @return
	 */
	public CorpOpenAccountRecord getCorpOpenAccountRecord(Integer userId);

	/**
	 * 根据用户Id查询出借记录数
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
