package com.hyjf.mybatis.mapper.customize.web;

import java.util.List;

import com.hyjf.mybatis.model.auto.StockInfo;
import com.hyjf.mybatis.model.customize.web.StockInfo2Customize;
import com.hyjf.mybatis.model.customize.web.StockInfoCustomize;

public interface StockInfoCustomizeMapper {

	/**
	 * 查询股票信息
	 * @param stockInfoCustomize
	 * @return
	 */
	public List<StockInfo> queryStockInfos(StockInfoCustomize stockInfoCustomize);
	

	/**
	 * 查询股票信息 实时图、5日图
	 * @param stockInfoCustomize
	 * @return
	 */
	public List<StockInfo2Customize> queryStockInfos2(StockInfoCustomize stockInfoCustomize);
	

	/**
	 * 查询股票信息 月图、年图
	 * @param stockInfoCustomize
	 * @return
	 */
	public List<StockInfo2Customize> queryStockInfosForMonthYear(StockInfoCustomize stockInfoCustomize);
	
	
}
