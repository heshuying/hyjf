package com.hyjf.admin.finance.merchant.transfer;

import java.util.List;

import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.admin.BaseService;
import com.hyjf.mybatis.model.auto.MerchantAccount;
import com.hyjf.mybatis.model.auto.MerchantTransfer;
import com.hyjf.mybatis.model.auto.UserTransfer;
import com.hyjf.mybatis.model.auto.Users;

public interface MerchantTransferService extends BaseService {

	/**
	 * 根据开户参数，获取开户信息
	 * 
	 * @param AccountListBean
	 * @param limitStart
	 * @param limitEnd
	 * @return
	 */

	public List<MerchantTransfer> selectRecordList(MerchantTransferListBean form, int limitStart, int limitEnd);

	/**
	 * 统计开户记录总数
	 * 
	 * @param AccountListBean
	 * @return
	 */

	public int queryRecordTotal(MerchantTransferListBean form);

	/**
	 * 校验商户子账户余额
	 * 
	 * @param outUserName
	 * @param transferAmount
	 * @param ret
	 */
	public void checkMerchantTransfer(String outAccountId, String transferAmount, JSONObject ret);

	/**
	 * 保存转账链接
	 * 
	 * @param form
	 * @return
	 */
	public boolean insertTransfer(MerchantTransferCustomizeBean form);

	/**
	 * 校验转账参数
	 * 
	 * @param modelAndView
	 * @param form
	 */
	public void checkMerchantTransferParam(ModelAndView modelAndView, MerchantTransferCustomizeBean form);

	/**
	 * 根据id获取转账信息
	 * 
	 * @param parseInt
	 * @return
	 */
	public UserTransfer searchUserTransferById(int parseInt);

	/**
	 * 根据userid获取用户信息
	 * 
	 * @param outUserId
	 * @return
	 */
	public Users searchUserByUserId(Integer outUserId);

	/**
	 * 查询子账户列表
	 * 
	 * @param i
	 * @return
	 */

	public List<MerchantAccount> selectMerchantAccountList(Integer i);

	/**
	 * 更新用户转账状态
	 * 
	 * @param orderId
	 * @param status
	 * @param string 
	 * @return
	 */

	public int updateMerchantTransfer(String orderId, int status, String string);

}
