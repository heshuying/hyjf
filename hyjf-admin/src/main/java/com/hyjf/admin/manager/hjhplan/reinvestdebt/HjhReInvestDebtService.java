package com.hyjf.admin.manager.hjhplan.reinvestdebt;

import java.util.List;

import com.hyjf.mybatis.model.customize.web.hjh.HjhReInvestDebtCustomize;

public interface HjhReInvestDebtService {
	
	/**
	 * 资金明细 （明细数量）
	 * @param accountManageBean
	 * @return
	 */
	public Integer queryHjhReInvestDebtCount(HjhReInvestDebtCustomize hjhReInvestDebtCustomize) ;
	
	/**
	 * 资金明细（列表）
	 * @param accountManageBean
	 * @return
	 */
	public List<HjhReInvestDebtCustomize> queryHjhReInvestDebts(HjhReInvestDebtCustomize hjhReInvestDebtCustomize); 
	
	/**
	 * 导出
	 * 
	 * @param hjhReInvestDebtCustomize
	 * @return
	 */
	public List<HjhReInvestDebtCustomize> exportReInvestDebts(HjhReInvestDebtCustomize hjhReInvestDebtCustomize) ; 
	
	/**
	 * 获取合计值
	 * @return
	 */
	HjhReInvestDebtCustomize queryReInvestDebtTotal(HjhReInvestDebtCustomize hjhReInvestDebtCustomize);
	
}

	