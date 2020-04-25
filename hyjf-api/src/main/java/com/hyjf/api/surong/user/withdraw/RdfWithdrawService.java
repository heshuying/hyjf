package com.hyjf.api.surong.user.withdraw;

import java.util.Map;

import com.hyjf.base.service.BaseService;

public interface RdfWithdrawService extends BaseService{
	/**
	 * 查询提现结果
	 * @param userId
	 * @param orderId
	 * @return
	 */
	public void getWithdrawResult(Integer userId,String orderId,Map<String, String> result);
	
	/**
	 * 根据银行名称获取银行logo
	 * @param name
	 * @return
	 */
	public String getBankLogoByBankName(String name);
}
