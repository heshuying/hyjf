package com.hyjf.mybatis.mapper.customize;

import com.hyjf.mybatis.model.auto.IncreaseInterestInvestExample;

/**
 * @author PC-LIUSHOUYI
 */

public interface IncreaseInterestInvestCustomizeMapper {

	/**
	 * 取得合计金额
	 * 
	 * @param param
	 * @return
	 */
	public String sumAccount(IncreaseInterestInvestExample example);
}

	