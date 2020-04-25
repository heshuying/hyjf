package com.hyjf.admin.manager.plan.accountdetail;

import java.util.List;

import org.springframework.stereotype.Service;

import com.hyjf.admin.BaseServiceImpl;
import com.hyjf.mybatis.model.auto.AccountTrade;
import com.hyjf.mybatis.model.auto.AccountTradeExample;
import com.hyjf.mybatis.model.customize.admin.htj.DebtAccountDetailCustomize;

@Service
public class HtjAccountDetailServiceImpl extends BaseServiceImpl implements HtjAccountDetailService {

	/**
	 * 查询符合条件的资金明细数量
	 * 
	 * @param accountInfoCustomize
	 * @return
	 * @author HBZ
	 */
	@Override
	public Integer queryAccountDetailCount(DebtAccountDetailCustomize accountDetailCustomize) {
		Integer accountCount = this.debtAccountDetailCustomizeMapper.queryAccountDetailCount(accountDetailCustomize);
		return accountCount;

	}

	/**
	 * 资金明细列表查询
	 * 
	 * @param accountInfoCustomize
	 * @return
	 * @author HBZ
	 */
	@Override
	public List<DebtAccountDetailCustomize> queryAccountDetails(DebtAccountDetailCustomize accountDetailCustomize) {
		List<DebtAccountDetailCustomize> accountInfos = this.debtAccountDetailCustomizeMapper.queryAccountDetails(accountDetailCustomize);
		return accountInfos;

	}

	/**
	 * 查询用户交易明细的交易类型
	 * 
	 * @return
	 */
	@Override
	public List<AccountTrade> selectTradeTypes() {
		AccountTradeExample example = new AccountTradeExample();
		AccountTradeExample.Criteria crt = example.createCriteria();
		crt.andStatusEqualTo(1);
		List<AccountTrade> list = accountTradeMapper.selectByExample(example);
		return list;
	}

}
