package com.hyjf.bank.service.user.deletecard;

import com.hyjf.bank.service.BaseService;
import com.hyjf.mybatis.model.auto.BankCard;
import com.hyjf.mybatis.model.auto.CorpOpenAccountRecord;
import com.hyjf.pay.lib.bank.bean.BankCallBean;
import org.springframework.web.servlet.ModelAndView;

/**
 * 用户删除银行卡Service
 * 
 * @author liuyang
 *
 */
public interface DeleteCardService extends BaseService {

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
	 * 根据用户Id,银行卡Id查询用户银行卡信息
	 * 
	 * @param userId
	 * @param cardId
	 * @return
	 */
	public BankCard getBankCardById(Integer userId, String cardId);
	   /**
     * 根据用户ID,银行卡号检索用户银行卡信息
     * 
     * @param userId
     * @param cardNo
     * @return
     */
    public BankCard getBankCardByCardNo(Integer userId, String cardNo);

	/**
	 * 根据银行卡编号获取银行卡信息
	 * @param userId
	 * @param cardNo
	 * @return
	 */
	BankCard getBankCardByCardNO(Integer userId, String cardNo);
	/**
	 * 解卡页面调用(合规四期需求)
	 * @param bean
	 * @return
	 */
	ModelAndView getCallbankMV(DeleteCardPageBean bean);

	boolean deleteBankCard(BankCallBean bean, Integer userId) throws Exception;
}
