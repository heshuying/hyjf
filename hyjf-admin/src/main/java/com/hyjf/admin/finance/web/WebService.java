package com.hyjf.admin.finance.web;

import java.util.List;

import com.hyjf.mybatis.model.auto.AccountTrade;
import com.hyjf.mybatis.model.auto.AccountWebList;
import com.hyjf.mybatis.model.customize.WebCustomize;

public interface WebService {
	
	/**
	 * 网站收支 （数量）
	 * @param webCustomize
	 * @return
	 */
	public Integer queryWebCount(WebCustomize webCustomize) ;
	
	/**
	 * 网站收支 （列表）
	 * @param webCustomize
	 * @return
	 */
	public List<WebCustomize> queryWebList(WebCustomize webCustomize) ;
	
	/**
	 * 查询公司余额
	 * @return
	 */
	double getCompanyYuE(String companyId);

	/**
	 * 查询用户交易明细的交易类型
	 * @return
	 */
	List<AccountTrade> selectTradeTypes();

	/**
	 * 插入网站收支记录
	 *
	 * @param accountWebList
	 * @return
	 */
	public int insertAccountWebList(AccountWebList accountWebList);

	/**
	 * 出借金额合计
	 * 
	 * @param borrowInvestCustomize
	 * @return
	 */
	public String selectBorrowInvestAccount(WebCustomize webCustomize);
	
}

	