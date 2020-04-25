/**
 * Description:我的出借service接口
 * Copyright: Copyright (HYJF Corporation) 2015
 * Company: HYJF Corporation
 * @author: 王坤
 * @version: 1.0
 * Created at: 2015年11月11日 下午2:17:31
 * Modification History:
 * Modified by : 
 */
package com.hyjf.web.user.planinfo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.hyjf.mybatis.model.auto.DebtPlan;
import com.hyjf.mybatis.model.auto.DebtPlanAccede;
import com.hyjf.mybatis.model.customize.DebtPlanBorrowCustomize;
import com.hyjf.mybatis.model.customize.PlanCommonCustomize;
import com.hyjf.mybatis.model.customize.PlanInvestCustomize;
import com.hyjf.mybatis.model.customize.PlanLockCustomize;
import com.hyjf.mybatis.model.customize.admin.htj.DebtBorrowCommonCustomize;
import com.hyjf.mybatis.model.customize.admin.htj.DebtBorrowCustomize;
import com.hyjf.mybatis.model.customize.batch.BatchDebtPlanAccedeCustomize;
import com.hyjf.mybatis.model.customize.web.WebProjectRepayListCustomize;
import com.hyjf.mybatis.model.customize.web.WebUserInvestListCustomize;
import com.hyjf.web.BaseService;
import com.hyjf.web.user.mytender.ProjectRepayListBean;
import com.hyjf.web.user.mytender.UserInvestListBean;

public interface PlanInfoService extends BaseService {

	/**
	 * 我的出借列表查询
	 * @param hzt
	 * @param i
	 * @param pageSize
	 * @return
	 */
	List<PlanLockCustomize> selectUserProjectList(Map<String, Object> params);
	
	/**
	 * 我的出借数据总数
	 * @param params
	 * @return
	 */
	Long countUserProjectRecordTotal(Map<String, Object> params);

	HashMap<String, Object> selectPlanInfoSum(String accedeOrderId);

	Long countDebtInvestList(Map<String, Object> params);

	List<PlanInvestCustomize> selectPlanInvestList(Map<String, Object> params);

	List<DebtPlan> getPlanByPlanNid(String planNid);

	BatchDebtPlanAccedeCustomize selectPlanAccedeInfo(
			DebtPlanAccede debtPlanAccede);

	List<DebtPlanBorrowCustomize> getDebtPlanBorrowList(
			Map<String, Object> param);

	List<DebtBorrowCustomize> selectBorrowList(
			DebtBorrowCommonCustomize debtBorrowCommonCustomize);

	int countProjectRepayPlanRecordTotal(ProjectRepayListBean bean);

	List<WebProjectRepayListCustomize> selectProjectRepayPlanList(
			ProjectRepayListBean bean, int offset, int limit);

	List<WebUserInvestListCustomize> selectUserInvestList(
			UserInvestListBean form, int i, int j);

	int countUserInvestRecordTotal(UserInvestListBean form);

	Long countDebtInvestListNew(Map<String, Object> params1);

	List<PlanInvestCustomize> selectPlanInvestListNew(
			Map<String, Object> params1);

	List<PlanInvestCustomize> selectInvestCreditList(Map<String, Object> params1);

	List<PlanInvestCustomize> selectCreditCreditList(Map<String, Object> params1);

    List<PlanLockCustomize> selectUserProjectListCapital(Map<String ,Object> params);
	
}
