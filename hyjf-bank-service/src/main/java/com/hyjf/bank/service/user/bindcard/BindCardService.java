package com.hyjf.bank.service.user.bindcard;

import java.util.List;

import org.springframework.web.servlet.ModelAndView;

import com.hyjf.bank.service.BaseService;
import com.hyjf.mybatis.model.auto.BankCard;
import com.hyjf.mybatis.model.auto.Users;
import com.hyjf.mybatis.model.auto.UsersInfo;
import com.hyjf.pay.lib.bank.bean.BankCallBean;

/**
 * 用户绑卡Serivce类
 * 
 * @author liuyang
 *
 */
public interface BindCardService extends BaseService {

	/**
	 * 用户绑卡后处理
	 * 
	 * @param bean
	 * @return
	 */
	public void updateAfterBindCard(BankCallBean bean) throws Exception;

	/**
	 * 获取用户的身份证号
	 * 
	 * @param userId
	 * @return 用户的身份证号
	 */
	public String getUserIdcard(Integer userId);

	/**
	 * 
	 * @method: getAccountBankByUserId
	 * @description: 根据userid查询绑卡信息
	 * @param logUserId
	 * @return
	 * @return: List<AccountBank>
	 * @mender: zhouxiaoshuai
	 * @date: 2017年3月28日 下午2:18:57
	 */
	public List<BankCard> getAccountBankByUserId(String logUserId);
	/**
	 * 
	 * 绑定银行卡发送短信
	 * @author pcc
	 * @param userId
	 * @param txcodeCardBindPlus
	 * @param mobile
	 * @param channelPc
	 * @return
	 */
    public BankCallBean cardBindPlusSendSms(Integer userId,String instCode, String txcodeCardBindPlus, String mobile, String channelPc,String cardNo);
    
    /**
	 * 
	 * 绑定银行卡发送短信
	 * @author pcc
	 * @param userId
	 * @param txcodeCardBindPlus
	 * @param mobile
	 * @param channelPc
	 * @return
	 */
    public BankCallBean cardBindPlusSendSms(Integer userId,String txcodeCardBindPlus, String mobile, String channelPc,String cardNo);
    
    /**
     * 根据用户ID取得用户信息(不加密)
     * @param userId
     * @return
     */
    public UsersInfo getUsersInfoByUserIdTrue(Integer userId);
    
    /**
     * 取得用户信息(不加密)
     */
    public Users getUsersTrue(Integer userId);
    /**
     * 新版开户绑卡页面共同
     * @param bean
     * @return
     */
	public ModelAndView getCallbankMV(BindCardPageBean bean);
	/**
	 * 用户绑卡回调方法
	 *
	 * @param bean
	 * @return
	 * @throws Exception
	 */
	public void updateCardNoToBank(BankCallBean bean);
	/**
	 * 判断江西银行绑卡使用新/旧接口
	 *
	 * @return 0旧接口/1新接口
	 */
	public Integer getBandCardBindUrlType(String interfaceType);
}
