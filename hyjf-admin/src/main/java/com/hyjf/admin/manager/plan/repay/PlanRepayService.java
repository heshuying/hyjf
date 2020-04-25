package com.hyjf.admin.manager.plan.repay;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.hyjf.admin.BaseService;
import com.hyjf.mybatis.model.auto.DebtLoan;
import com.hyjf.mybatis.model.auto.DebtPlan;
import com.hyjf.mybatis.model.auto.DebtPlanAccede;
import com.hyjf.mybatis.model.customize.PlanCommonCustomize;
import com.hyjf.mybatis.model.customize.PlanInvestCustomize;
import com.hyjf.mybatis.model.customize.PlanLockCustomize;

/**
 * 计划已还款列表Service
 * 
 * @ClassName PlanRepayService
 * @author liuyang
 * @date 2016年9月26日 下午4:40:01
 */
public interface PlanRepayService extends BaseService {

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

    public Long countLoanDetail(PlanCommonCustomize planCommonCustomize);

    public List<Map<String, Object>> selectLoanDetailList(PlanCommonCustomize planCommonCustomize);

    public HashMap<String, Object> LoanDeailSumMap(PlanCommonCustomize planCommonCustomize);

    public Long countLoanDetailNew(PlanCommonCustomize planCommonCustomize);

    public HashMap<String, Object> LoanDeailSumMapNew(PlanCommonCustomize planCommonCustomize);

    public List<Map<String, Object>> selectLoanDetailListNew(PlanCommonCustomize planCommonCustomize);

    public boolean repayNow(String plannid);

    public HashMap<String, Object> selectPlanCreditCountMap(String planNidSrch);

	int countPlan(PlanCommonCustomize planCommonCustomize);

	List<DebtPlan> selectPlanList(PlanCommonCustomize planCommonCustomize);
}
