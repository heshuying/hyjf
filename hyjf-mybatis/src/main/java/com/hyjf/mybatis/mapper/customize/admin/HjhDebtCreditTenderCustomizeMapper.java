package com.hyjf.mybatis.mapper.customize.admin;

import java.util.List;
import java.util.Map;

import com.hyjf.mybatis.model.customize.admin.HjhDebtCreditTenderCustomize;

public interface HjhDebtCreditTenderCustomizeMapper {

	/**
	 * 获取列表
	 * 
	 * @param DebtCreditCustomize
	 * @return
	 */
	List<HjhDebtCreditTenderCustomize> selectDebtCreditTenderList(Map<String, Object> param);

	/**
	 * COUNT
	 * 
	 * @param DebtCreditCustomize
	 * @return
	 */
	Integer countDebtCreditTender(Map<String, Object> param);

	/**
	 * 查询列表的个别统计数据
	 * @param param
	 * @return
     */
	Map<String,Object> selectSumTotal(Map<String,Object> param);

}