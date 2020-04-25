package com.hyjf.mybatis.mapper.customize.admin.htj;

import java.util.List;
import java.util.Map;

import com.hyjf.mybatis.model.customize.admin.htj.DebtCreditTenderCustomize;

public interface DebtCreditTenderCustomizeMapper {

	/**
	 * 获取列表
	 * 
	 * @param DebtCreditCustomize
	 * @return
	 */
	List<DebtCreditTenderCustomize> selectDebtCreditTenderList(Map<String, Object> param);

	/**
	 * COUNT
	 * 
	 * @param DebtCreditCustomize
	 * @return
	 */
	Integer countDebtCreditTender(Map<String, Object> param);

}