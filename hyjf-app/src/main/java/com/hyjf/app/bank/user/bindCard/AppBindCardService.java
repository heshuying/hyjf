package com.hyjf.app.bank.user.bindCard;

import java.util.List;

import com.hyjf.app.BaseService;
import com.hyjf.mybatis.model.auto.BankCard;
import com.hyjf.mybatis.model.auto.Users;
import com.hyjf.mybatis.model.auto.UsersInfo;
import com.hyjf.pay.lib.bank.bean.BankCallBean;

/**
 * App绑卡Service类
 * 
 * @author liuyang
 *
 */
public interface AppBindCardService extends BaseService {

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
	 * 绑定成功更新银行卡信息
	 * 
	 * @param userId
	 * @return 用户的身份证号
	 */
	public void updateAccountBankByUserId(Integer userId);

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
	 * 根据id查询用户
	 * @param userId
	 * @return
	 */
	public Users getUsers(Integer userId);

	/**
	 * 获取userInfo
	 * @param userId
	 * @return
	 */
	public UsersInfo getUsersInfoByUserId(Integer userId);

    /**
     * 根据用户ID取得用户信息(不加密)
     * @param userId
     * @return
     */
    public UsersInfo getUsersInfoByUserIdTrue(Integer userId);

    /**
     * 取得用户信息(不加密)
     *
     */
    public Users getUsersTrue(Integer userId);
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
    public BankCallBean cardBindPlusSendSms(Integer userId, String txcodeCardBindPlus, String mobile, String channelPc,
        String cardNo);

}
