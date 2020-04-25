package com.hyjf.web.stock;

import java.util.List;

import com.hyjf.mybatis.model.auto.StockInfo;
import com.hyjf.mybatis.model.customize.web.StockInfo2Customize;
import com.hyjf.web.BaseService;

public interface StockInfoService extends BaseService {

	/**
	 * 查询股票数据
	 * @return
	 */

	List<StockInfo> queryStockInfoList(String type);
	/**
	 * 查询股票数据
	 * @return
	 */

	List<StockInfo2Customize> queryStockInfoList2(String type);

	/**
	 * 查询当前股票数据
	 * @return
	 */

	StockInfo queryStockInfo();
}
