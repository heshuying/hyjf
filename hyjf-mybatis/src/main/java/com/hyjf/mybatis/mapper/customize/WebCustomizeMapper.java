package com.hyjf.mybatis.mapper.customize;

import java.util.List;

import com.hyjf.mybatis.model.customize.WebCustomize;

public interface WebCustomizeMapper {
	/**
	 * 网站收支（收支数量）
	 * @param accountManageBean
	 * @return
	 */
	public Integer queryWebCount(WebCustomize webCustomize) ;

	/**
	 * 充值管理 （列表）
	 * @param accountManageBean
	 * @return
	 */
	public List<WebCustomize> queryWebList(WebCustomize webCustomize) ;         
	
	/**
	 * 出借金额合计
	 * 
	 * @param borrowInvestCustomize
	 * @return
	 */
	public String selectBorrowInvestAccount(WebCustomize webCustomize);
	
}

	