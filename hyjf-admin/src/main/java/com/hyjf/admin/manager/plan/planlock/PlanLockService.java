
package com.hyjf.admin.manager.plan.planlock;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.mybatis.model.auto.DebtLoan;
import com.hyjf.mybatis.model.auto.DebtPlan;
import com.hyjf.mybatis.model.auto.DebtPlanAccede;
import com.hyjf.mybatis.model.customize.PlanCommonCustomize;
import com.hyjf.mybatis.model.customize.PlanInvestCustomize;
import com.hyjf.mybatis.model.customize.PlanLockCustomize;
import com.hyjf.mybatis.model.customize.admin.htj.DebtAdminCreditCustomize;

public interface PlanLockService {

	/**
	 * 计划列表查询件数
	 * 
	 * @method: countPlan
	 * @return: List<DebtPlanType>
	 * @mender: zhouxiaoshuai
	 * @date: 2016年9月12日 下午5:57:15
	 */
	public int countPlan(PlanCommonCustomize planCommonCustomize);

	/**
	 * 计划列表查询
	 * 
	 * @method: selectPlanList
	 * @return: List<DebtPlanType>
	 * @mender: zhouxiaoshuai
	 * @date: 2016年9月12日 下午5:57:15
	 */
	public List<DebtPlan> selectPlanList(PlanCommonCustomize planCommonCustomize);

	/**
	 * 
	 * @method: exportPlanList
	 * @description: 计划列表导出
	 * @return
	 * @return: List<DebtPlanType>
	 * @mender: zhouxiaoshuai
	 * @date: 2016年9月12日 下午5:57:15
	 */
	public List<DebtPlan> exportPlanList(PlanCommonCustomize planCommonCustomize);

	public Long countPlanAccede(PlanCommonCustomize planCommonCustomize);

	public List<PlanLockCustomize> selectPlanAccedeList(PlanCommonCustomize planCommonCustomize);

	public HashMap<String, Object> selectPlanCountMap(String planNidSrch);

	public HashMap<String, Object> planLockSumMap(PlanCommonCustomize planCommonCustomize);

	public int updateCycleTimesZero(String accedeorderId);

	public List<PlanInvestCustomize> selectPlanInvestList(PlanCommonCustomize planCommonCustomize);

	public Long countPlanInvest(PlanCommonCustomize planCommonCustomize);

	public HashMap<String, Object> planInvestSumMap(PlanCommonCustomize planCommonCustomize);

	public int countDebtLoan(PlanCommonCustomize planCommonCustomize);

	public List<DebtLoan> selectDebtLoanList(PlanCommonCustomize planCommonCustomize);

	public HashMap<String, Object> DebtLoanSumMap(PlanCommonCustomize planCommonCustomize);

	public List<DebtPlanAccede> getDebtPlanAccedes(String accedeOrderId);

	public boolean tender(DebtPlanAccede debtPlanAccede, String borrowNid, BigDecimal money, BigDecimal minSurplusInvestAccount);

	public JSONObject checkParamAppointment(String borrowNid, String money, Integer userId);

	public Long countLoanDetail(PlanCommonCustomize planCommonCustomize);

	public List<Map<String, Object>> selectLoanDetailList(PlanCommonCustomize planCommonCustomize);

	public HashMap<String, Object> LoanDeailSumMap(PlanCommonCustomize planCommonCustomize);

	public Long countLoanDetailNew(PlanCommonCustomize planCommonCustomize);

	public HashMap<String, Object> LoanDeailSumMapNew(PlanCommonCustomize planCommonCustomize);

	public List<Map<String, Object>> selectLoanDetailListNew(PlanCommonCustomize planCommonCustomize);

	/**
	 * 检索汇添金转让类产品件数
	 * 
	 * @Title countCreditProject
	 * @param param
	 * @return
	 */
	public int countCreditProject(Map<String, Object> param);

	/**
	 * 检索汇添金转让类产品列表
	 * 
	 * @Title selectDebtCreditProject
	 * @param param
	 * @return
	 */
public List<DebtAdminCreditCustomize> selectDebtCreditProject(Map<String, Object> param);

	/**
	 * 根据加入订单号查询加入详情列表
	 * 
	 * @Title selectDebtPlanAccedeByAccedeOrderId
	 * @param accedeOrderId
	 * @return
	 */
	public List<DebtPlanAccede> selectDebtPlanAccedeByAccedeOrderId(String accedeOrderId);

	public Long countPlanInvestNew(PlanCommonCustomize planCommonCustomize);

	public HashMap<String, Object> planInvestSumMapNew(PlanCommonCustomize planCommonCustomize);

	public List<PlanInvestCustomize> selectPlanInvestListNew(PlanCommonCustomize planCommonCustomize);
	
	/**
	 * 计算计划到期年化利率
	 * @Title selectExpectApr
	 * @param plan
	 * @return
	 */
	public String calculationPlanExpectApr(DebtPlan plan);
	
	/**
	 * 根据计划编号检索计划详情
	 * @Title selectDebtPlanInfoByPlanNid
	 * @param planNid
	 * @return
	 */
	public DebtPlan selectDebtPlanInfoByPlanNid(String planNid);
}