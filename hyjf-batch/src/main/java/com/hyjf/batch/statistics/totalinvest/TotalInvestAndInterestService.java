package com.hyjf.batch.statistics.totalinvest;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * @author xiasq
 * @version TotalInvestAndInterestService, v0.1 2018/5/16 10:18
 */
public interface TotalInvestAndInterestService {
	void execute() throws Exception;

	/**
	 * 累计交易笔数
	 * 
	 * @return
	 */
	int countTotalInvestNum();

	/**
	 * 累计为用户赚取收益
	 * 
	 * @return
	 */
	BigDecimal countTotalInterestAmount();

	/**
	 * 累计交易总额
	 * 
	 * @return
	 */
	BigDecimal countTotalInvestAmount();

	/**
	 * 查询汇计划数据
	 */
	List<Map<String, Object>> searchPlanStatisticData();
}
