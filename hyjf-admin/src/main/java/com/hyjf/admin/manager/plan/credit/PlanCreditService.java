package com.hyjf.admin.manager.plan.credit;

import java.util.List;
import java.util.Map;

import com.hyjf.admin.BaseService;
import com.hyjf.mybatis.model.auto.BorrowStyle;
import com.hyjf.mybatis.model.auto.Users;
import com.hyjf.mybatis.model.customize.admin.htj.DebtCreditCustomize;

public interface PlanCreditService extends BaseService {

	/**
	 * COUNT
	 * 
	 * @param borrowCustomize
	 * @return
	 */
	public Integer countDebtCredit(Map<String, Object> params);

	/**
	 * 借款列表
	 * 
	 * @return
	 */
	public List<DebtCreditCustomize> selectDebtCreditList(Map<String, Object> params);

	/**
	 * 获取用户名
	 * 
	 * @return
	 */
	public Users getUsers(Integer userId);

	public List<BorrowStyle> selectBorrowStyleList(String string);

}
