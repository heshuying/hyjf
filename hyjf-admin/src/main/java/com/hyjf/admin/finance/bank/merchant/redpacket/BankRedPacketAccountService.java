package com.hyjf.admin.finance.bank.merchant.redpacket;

import java.util.List;

import com.hyjf.admin.BaseService;
import com.hyjf.mybatis.model.customize.admin.BankMerchantAccountListCustomize;

public interface BankRedPacketAccountService extends BaseService {

	/**
	 * 获取商户子账户列表
	 * 
	 * @param BankPoundageAccountListBean
	 * @param limitStart
	 * @param limitEnd
	 * @return
	 */

	public List<BankMerchantAccountListCustomize> selectRecordList(BankRedPacketAccounttListBean form);

	/**
	 * 获取商户子账户总数
	 * 
	 * @param BankPoundageAccountListBean
	 * @return
	 */

	public int queryRecordTotal(BankRedPacketAccounttListBean form);


}
