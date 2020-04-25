package com.hyjf.admin.manager.plan.credit.tender;

import java.util.List;
import java.util.Map;

import com.hyjf.admin.BaseService;
import com.hyjf.mybatis.model.auto.BorrowStyle;
import com.hyjf.mybatis.model.customize.admin.htj.DebtCreditTenderCustomize;

public interface PlanCreditTenderService extends BaseService {


	/**
	 * 获取详细列表COUNT
	 * 
	 * @param params
	 * @return
	 */
	public Integer countDebtCreditTenderList(Map<String, Object> params);

	/**
	 * 获取详细列表
	 * 
	 * @param DebtCreditCustomize
	 * @return
	 */
	public List<DebtCreditTenderCustomize> selectDebtCreditTenderList(Map<String, Object> params);

	/**
	 * @param string
	 * @return
	 */
		
	public List<BorrowStyle> selectBorrowStyleList(String string);


}
