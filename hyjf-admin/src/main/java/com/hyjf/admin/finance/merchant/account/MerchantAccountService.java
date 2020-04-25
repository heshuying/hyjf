package com.hyjf.admin.finance.merchant.account;

import java.util.List;

import com.hyjf.admin.BaseService;
import com.hyjf.mybatis.model.auto.MerchantAccount;
import com.hyjf.mybatis.model.customize.admin.AdminMerchantAccountSumCustomize;

public interface MerchantAccountService extends BaseService {

	/**
	 * 获取商户子账户列表
	 * 
	 * @param MerchantAccountListBean
	 * @param limitStart
	 * @param limitEnd
	 * @return
	 */

	public List<MerchantAccount> selectRecordList(MerchantAccountListBean form, int limitStart,int limitEnd);

	/**
	 * 获取商户子账户总数
	 * 
	 * @param MerchantAccountListBean
	 * @return
	 */

	public int queryRecordTotal(MerchantAccountListBean form);

	/**
	 * 更新商户子账户金额
	 * @return 
	 * 
	 */
		
	public boolean updateMerchantAccount();

	/**
	 * 查询子账户总额
	 * @return
	 */
		
	public AdminMerchantAccountSumCustomize searchAccountSum();


}
