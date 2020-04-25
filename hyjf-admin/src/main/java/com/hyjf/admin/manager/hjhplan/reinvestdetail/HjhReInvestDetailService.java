package com.hyjf.admin.manager.hjhplan.reinvestdetail;

import java.util.List;

import com.hyjf.mybatis.model.customize.web.hjh.HjhReInvestDetailCustomize;

public interface HjhReInvestDetailService {
	
	/**
	 * 资金明细 （明细数量）
	 * @param accountManageBean
	 * @return
	 */
	public Integer queryHjhReInvestDetailCount(HjhReInvestDetailCustomize hjhReInvestDetailCustomize) ;
	
	/**
	 * 资金明细（列表）
	 * @param accountManageBean
	 * @return
	 */
	public List<HjhReInvestDetailCustomize> queryHjhReInvestDetails(HjhReInvestDetailCustomize hjhReInvestDetailCustomize); 
	
	/**
	 * 导出
	 * 
	 * @param hjhReInvestDetailCustomize
	 * @return
	 */
	public List<HjhReInvestDetailCustomize> exportReInvestDetails(HjhReInvestDetailCustomize hjhReInvestDetailCustomize) ; 
	
	/**
	 * 获取合计值
	 * @return
	 */
	String queryReInvestDetailTotal(HjhReInvestDetailCustomize hjhReInvestDetailCustomize);
	
}

	