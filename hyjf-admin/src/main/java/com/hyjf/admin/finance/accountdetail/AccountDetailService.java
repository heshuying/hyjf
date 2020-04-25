package com.hyjf.admin.finance.accountdetail;

import java.util.List;

import com.hyjf.mybatis.model.auto.AccountTrade;
import com.hyjf.mybatis.model.customize.AccountDetailCustomize;

public interface AccountDetailService {

	/**
	 * 资金明细 （明细数量）
	 * 
	 * @param accountManageBean
	 * @return
	 */
	public Integer queryAccountDetailCount(AccountDetailCustomize accountDetailCustomize);

	/**
	 * 资金明细（列表）
	 * 
	 * @param accountManageBean
	 * @return
	 */
	public List<AccountDetailCustomize> queryAccountDetails(AccountDetailCustomize accountDetailCustomize);

	/**
	 * 查询用户交易明细的交易类型
	 * 
	 * @return
	 */
	List<AccountTrade> selectTradeTypes();

	/**
	 * 20170120还款后交易明细修改
	 */
	public void repayDataRepair() throws Exception;

}
