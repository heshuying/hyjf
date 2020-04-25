package com.hyjf.admin.manager.plan.accountdetail;

import java.util.List;

import com.hyjf.mybatis.model.auto.AccountTrade;
import com.hyjf.mybatis.model.customize.admin.htj.DebtAccountDetailCustomize;

public interface HtjAccountDetailService {
	
	/**
	 * 资金明细 （明细数量）
	 * @param accountManageBean
	 * @return
	 */
	public Integer queryAccountDetailCount(DebtAccountDetailCustomize accountDetailCustomize) ;
	
	/**
	 * 资金明细（列表）
	 * @param accountManageBean
	 * @return
	 */
	public List<DebtAccountDetailCustomize> queryAccountDetails(DebtAccountDetailCustomize accountDetailCustomize) ;      
	
	/**
	 * 查询用户交易明细的交易类型
	 * @return
	 */
	List<AccountTrade> selectTradeTypes();
	
}

	