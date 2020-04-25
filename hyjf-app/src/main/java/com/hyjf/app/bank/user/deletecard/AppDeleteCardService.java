package com.hyjf.app.bank.user.deletecard;

import java.math.BigDecimal;

import com.hyjf.app.BaseService;
import com.hyjf.mybatis.model.auto.BankCard;
import com.hyjf.pay.lib.bank.bean.BankCallBean;

/**
 * App解绑银行卡Service
 * 
 * @author liuyang
 *
 */
public interface AppDeleteCardService extends BaseService {

	/**
	 * 用户删除卡后处理
	 * 
	 * @param bean
	 * @return
	 */
	public boolean updateAfterDeleteCard(BankCallBean bean, Integer userId) throws Exception;

	/**
	 * 获取用户的身份证号
	 * 
	 * @param userId
	 * @return 用户的身份证号
	 */
	public String getUserIdcard(Integer userId);

	/**
	 * 根据电子账号查询用户在江西银行的可用余额
	 * 
	 * @param userId
	 * @param accountId
	 * @return
	 */
	public BigDecimal getBankBalance(Integer userId, String accountId);

	/**
	 * 根据用户ID,银行卡号检索用户银行卡信息
	 * 
	 * @param userId
	 * @param cardNo
	 * @return
	 */
	public BankCard getBankCardByCardNo(Integer userId, String cardNo);
	/**
	 * 判断江西银行绑卡使用新/旧接口
	 *
	 * @return 0旧接口/1新接口
	 */
	public Integer getBandCardBindUrlType(String string);

	/**
	 * 解绑银行卡后(异步回调删除)
	 * 合规四期(解卡页面调用)
	 *
	 * @param bean
	 * @return
	 * @throws Exception
	 */
	boolean deleteBankCard(BankCallBean bean, Integer userId) throws Exception;
	//获取绑卡页面 add by nxl
	String getResultUrl(String userId);

}
