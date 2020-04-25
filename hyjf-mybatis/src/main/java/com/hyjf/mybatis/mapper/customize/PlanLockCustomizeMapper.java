package com.hyjf.mybatis.mapper.customize;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.hyjf.mybatis.model.customize.CreditDetailCustomize;
import com.hyjf.mybatis.model.customize.PlanCommonCustomize;
import com.hyjf.mybatis.model.customize.PlanInvestCustomize;
import com.hyjf.mybatis.model.customize.PlanLockCustomize;

public interface PlanLockCustomizeMapper {
	/**
	 * 
	 * @method: getPlanTypeList
	 * @description: 计划数量查询
	 * @return
	 * @return: List<DebtPlanType>
	 * @mender: zhouxiaoshuai
	 * @date: 2016年9月12日 下午5:57:15
	 */
	Long countPlanAccede(PlanCommonCustomize planCommonCustomize);
	
	/**
	 * 
	 * 后台运营信息中的加入明细
	 * @author hsy
	 * @param planCommonCustomize
	 * @return
	 */
	Long countPlanAccedeForAdmin(PlanCommonCustomize planCommonCustomize);

	/**
	 * 
	 * @method: selectPlanList
	 * @description: 计划列表查询
	 * @return
	 * @return: List<DebtPlanType>
	 * @mender: zhouxiaoshuai
	 * @date: 2016年9月12日 下午5:57:15
	 */
	List<PlanLockCustomize> selectPlanAccedeList(PlanCommonCustomize planCommonCustomize);
	
	/**
	 * 
	 * 后台运营信息中的加入明细
	 * @author hsy
	 * @param planCommonCustomize
	 * @return
	 */
	List<PlanLockCustomize> selectPlanAccedeListForAdmin(PlanCommonCustomize planCommonCustomize);

	/**
	 * 
	 * @method: selectPlanCountMap
	 * @description: 查询计划加入笔数等统计
	 * @param planNidSrch
	 * @return
	 * @return: HashMap<String,Object>
	 * @mender: zhouxiaoshuai
	 * @date: 2016年9月26日 下午1:48:54
	 */
	HashMap<String, Object> selectPlanCountMap(String planNidSrch);

	HashMap<String, Object> planLockSumMap(PlanCommonCustomize planCommonCustomize);

	/**
	 * 
	 * @method: updateCycleTimesZero
	 * @description: 清零次数
	 * @param accedeorderId
	 * @return
	 * @return: int
	 * @mender: zhouxiaoshuai
	 * @date: 2016年9月26日 下午4:06:31
	 */
	int updateCycleTimesZero(String accedeorderId);

	/**
	 * 
	 * @method: selectPlanInvestList
	 * @description: 查询债权列表
	 * @param planCommonCustomize
	 * @return
	 * @return: List<PlanInvestCustomize>
	 * @mender: zhouxiaoshuai
	 * @date: 2016年9月27日 上午9:44:57
	 */
	List<PlanInvestCustomize> selectPlanInvestList(PlanCommonCustomize planCommonCustomize);

	/**
	 * 
	 * @method: countPlanInvest
	 * @description: 债权列表总数
	 * @param planCommonCustomize
	 * @return
	 * @return: Long
	 * @mender: zhouxiaoshuai
	 * @date: 2016年9月27日 上午9:45:26
	 */
	Long countPlanInvest(PlanCommonCustomize planCommonCustomize);

	/**
	 * 
	 * @method: planInvestSumMap
	 * @description: 债权列表总和
	 * @param planCommonCustomize
	 * @return
	 * @return: Long
	 * @mender: zhouxiaoshuai
	 * @date: 2016年9月27日 上午9:45:26
	 */
	HashMap<String, Object> planInvestSumMap(PlanCommonCustomize planCommonCustomize);

	HashMap<String, Object> DebtLoanSumMap(PlanCommonCustomize planCommonCustomize);

	List<PlanInvestCustomize> selectCreditInvestList(CreditDetailCustomize creditDetailCustomize);

	Long countCreditInvest(CreditDetailCustomize creditDetailCustomize);

	HashMap<String, Object> creditInvestSumMap(CreditDetailCustomize creditDetailCustomize);

	Long countLoanDetail(PlanCommonCustomize planCommonCustomize);

	List<Map<String, Object>> selectLoanDetailList(PlanCommonCustomize planCommonCustomize);

	HashMap<String, Object> LoanDeailSumMap(PlanCommonCustomize planCommonCustomize);

	HashMap<String, Object> selectPlanInfoSum(String accedeOrderId);

	Long countInvest(Map<String, Object> params);

	List<PlanInvestCustomize> selectInvestList(Map<String, Object> params);

	List<Map<String, Object>> selectLoanDetailListNew(PlanCommonCustomize planCommonCustomize);

	Long countLoanDetailNew(PlanCommonCustomize planCommonCustomize);

	HashMap<String, Object> LoanDeailSumMapNew(PlanCommonCustomize planCommonCustomize);

	Long updatePlanActualApr(String planNid);

	HashMap<String, Object> selectPlanCreditCountMap(String planNidSrch);

	Long countPlanInvestNew(PlanCommonCustomize planCommonCustomize);

	HashMap<String, Object> planInvestSumMapNew(PlanCommonCustomize planCommonCustomize);

	List<PlanInvestCustomize> selectPlanInvestListNew(PlanCommonCustomize planCommonCustomize);

	Long countPlanInvestNew(CreditDetailCustomize creditDetailCustomize);

	HashMap<String, Object> creditInvestSumMapNew(CreditDetailCustomize creditDetailCustomize);

	List<PlanInvestCustomize> selectPlanInvestListNew(CreditDetailCustomize creditDetailCustomize);

	Long countCreditInvestNew(CreditDetailCustomize creditDetailCustomize);

	List<PlanInvestCustomize> selectCreditInvestListNew(CreditDetailCustomize creditDetailCustomize);

	Long countInvestNew(Map<String, Object> params);

	List<PlanInvestCustomize> selectInvestListNew(Map<String, Object> params);

	List<PlanInvestCustomize> selectInvestCreditList(Map<String, Object> params1);

	List<PlanInvestCustomize> selectCreditCreditList(Map<String, Object> params1);
	
	
	public HashMap<String, Object> sumPlanExpireFairValue(String debtPlanNid); 

}