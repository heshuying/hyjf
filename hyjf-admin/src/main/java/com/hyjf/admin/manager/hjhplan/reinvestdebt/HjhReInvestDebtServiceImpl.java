package com.hyjf.admin.manager.hjhplan.reinvestdebt;

import java.util.List;

import org.springframework.stereotype.Service;

import com.hyjf.admin.BaseServiceImpl;
import com.hyjf.mybatis.model.customize.web.hjh.HjhReInvestDebtCustomize;

@Service
public class HjhReInvestDebtServiceImpl extends BaseServiceImpl implements HjhReInvestDebtService {

	/**
	 * 查询符合条件的资金明细数量
	 * 
	 * @param hjhReInvestDebtCustomize
	 * @return
	 * @author HJH
	 */
	@Override
	public Integer queryHjhReInvestDebtCount(HjhReInvestDebtCustomize hjhReInvestDebtCustomize) {
		Integer accountCount = this.hjhReInvestDebtCustomizeMapper.queryReInvestDebtCount(hjhReInvestDebtCustomize);
		return accountCount;
	}

	/**
	 * 资金明细列表查询
	 * 
	 * @param hjhReInvestDebtCustomize
	 * @return
	 * @author HJH
	 */
	@Override
	public List<HjhReInvestDebtCustomize> queryHjhReInvestDebts(HjhReInvestDebtCustomize hjhReInvestDebtCustomize) {
		List<HjhReInvestDebtCustomize> accountInfos = this.hjhReInvestDebtCustomizeMapper.queryReInvestDebts(hjhReInvestDebtCustomize);
		return accountInfos;

	}

	/**
	 * 查询用户交易明细的交易类型
	 * 
	 * @return
	 */
	@Override
	public HjhReInvestDebtCustomize queryReInvestDebtTotal(HjhReInvestDebtCustomize hjhReInvestDebtCustomize) {
		HjhReInvestDebtCustomize total = this.hjhReInvestDebtCustomizeMapper.queryReInvestDebtTotal(hjhReInvestDebtCustomize);
		return total;
	}

	/**
	 * 执行前每个方法前需要添加BusinessDesc描述
	 * @param hjhReInvestDebtCustomize
	 * @return
	 * @author PC-LIUSHOUYI
	 */
		
	@Override
	public List<HjhReInvestDebtCustomize> exportReInvestDebts(
			HjhReInvestDebtCustomize hjhReInvestDebtCustomize) {
		return this.hjhReInvestDebtCustomizeMapper.exportReInvestDebts(hjhReInvestDebtCustomize);
	}

}
