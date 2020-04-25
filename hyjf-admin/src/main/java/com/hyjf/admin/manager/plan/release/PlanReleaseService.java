package com.hyjf.admin.manager.plan.release;

import java.util.List;
import java.util.Map;

import com.hyjf.admin.BaseService;
import com.hyjf.mybatis.model.auto.DebtPlan;
import com.hyjf.mybatis.model.auto.DebtPlanWithBLOBs;
import com.hyjf.mybatis.model.customize.DebtPlanBorrowCustomize;
import com.hyjf.mybatis.model.customize.PlanCommonCustomize;

/**
 * 计划发布Service
 * 
 * @ClassName PlanReleaseService
 * @author liuyang
 * @date 2016年9月23日 上午11:11:26
 */
public interface PlanReleaseService extends BaseService {
	/**
	 * 检索计划发布列表件数
	 * 
	 * @Title countPlanRelease
	 * @param planCommonCustomize
	 * @return
	 */
	public int countPlanRelease(PlanCommonCustomize planCommonCustomize);

	/**
	 * 检索计划发布列表
	 * 
	 * @Title selectPlanReleaseList
	 * @param planCommonCustomize
	 * @return
	 */
	public List<DebtPlan> selectPlanReleaseList(PlanCommonCustomize planCommonCustomize);

	/**
	 * 根据计划编号检索计划详情
	 * 
	 * @Title selectPlanReleaseInfoByDebtPlanNid
	 * @param debtPlanNid
	 * @return
	 */
	public DebtPlan selectPlanReleaseInfoByDebtPlanNid(String debtPlanNid);

	/**
	 * 根据计划编号获取计划详情
	 * 
	 * @Title selectPlanInfoWithBLOBsByDebtPlanNid
	 * @param debtPlanNid
	 * @return
	 */
	public DebtPlanWithBLOBs selectPlanInfoWithBLOBsByDebtPlanNid(String debtPlanNid);

	/**
	 * 确认提审,更新计划状态
	 * 
	 * @Title updatePlanRecord
	 * @param deptPlanNid
	 */
	public void updatePlanRecord(String deptPlanNid);

	/**
	 * 根据计划编号检索计划关联的资产配置
	 * 
	 * @Title selectDebtPlanBorrowListByDebtPlanNid
	 * @param param
	 * @return
	 */
	public List<DebtPlanBorrowCustomize> selectDebtPlanBorrowListByDebtPlanNid(Map<String, Object> param);

	/**
	 * 获取计划编号检索计划关联的资产配置件数
	 * 
	 * @Title countDebtPlanBorrowListByDebtPlanNid
	 * @param param
	 * @return
	 */
	public int countDebtPlanBorrowListByDebtPlanNid(Map<String, Object> param);

	/**
	 * 审核后,计划信息更新
	 * 
	 * @Title updatePlanInfo
	 * @param form
	 */
	public void updatePlanInfo(PlanReleaseBean form);
	
	/**
	 * 检索已关联资产总计
	 * @Title countDebtPlanBorrowListAmount
	 * @param param
	 * @return
	 */
	public Map<String, Object> countDebtPlanBorrowListAmount(Map<String, Object> param);
}
