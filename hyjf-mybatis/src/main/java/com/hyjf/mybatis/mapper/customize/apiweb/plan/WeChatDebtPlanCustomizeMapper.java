package com.hyjf.mybatis.mapper.customize.apiweb.plan;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.hyjf.mybatis.model.auto.DebtPlan;
import com.hyjf.mybatis.model.customize.apiweb.plan.WeChatDebtPlanAccedeCustomize;
import com.hyjf.mybatis.model.customize.apiweb.plan.WeChatDebtPlanBorrowCustomize;
import com.hyjf.mybatis.model.customize.apiweb.plan.WeChatDebtPlanCustomize;
import com.hyjf.mybatis.model.customize.apiweb.plan.WeChatDebtPlanDetailCustomize;
import com.hyjf.mybatis.model.customize.apiweb.plan.WeChatDebtPlanIntroduceCustomize;
import com.hyjf.mybatis.model.customize.apiweb.plan.WeChatDebtPlanInvestInfoCustomize;
import com.hyjf.mybatis.model.customize.apiweb.plan.WeChatDebtPlanQuestionCustomize;
import com.hyjf.mybatis.model.customize.apiweb.plan.WeChatDebtPlanRiskControlCustomize;

public interface WeChatDebtPlanCustomizeMapper {

	/**
	 * 查询相应的计划列表信息
	 * 
	 * @param params
	 * @return
	 */
	List<WeChatDebtPlanCustomize> selectDebtPlanList(Map<String, Object> params);

	/**
	 * 统计相应的项目列表总数
	 * 
	 * @param params
	 * @return
	 */
	int queryDebtPlanRecordTotal(Map<String, Object> params);

	/**
	 * 查询相应的项目详情
	 * 
	 * @param borrowNid
	 * @return
	 */
	WeChatDebtPlanDetailCustomize selectDebtPlanDetail(@Param("planNid") String planNid);

	/***
	 * 查询相应的计划预览
	 * 
	 * @param borrowNid
	 * @return
	 */
	WeChatDebtPlanDetailCustomize selectDebtPlanPreview(@Param("planNid") String planNid);

	/**
	 * 查询相应的计划介绍
	 * 
	 * @param planNid
	 * @return
	 */

	WeChatDebtPlanIntroduceCustomize selectDebtPlanIntroduce(@Param("planNid") String planNid);

	/**
	 * 查询相应的计划的风控信息
	 * 
	 * @param planNid
	 * @return
	 */

	WeChatDebtPlanRiskControlCustomize selectDebtPlanRiskControl(@Param("planNid") String planNid);

	/**
	 * 查询相应的计划问题
	 * 
	 * @param planNid
	 * @return
	 */

	WeChatDebtPlanQuestionCustomize selectDebtPlanQuestion(@Param("planNid") String planNid);

	/**
	 * 查询相应的计划标的总数
	 * 
	 * @param params
	 * @return
	 */

	int countPlanBorrowRecordTotal(Map<String, Object> params);

	/**
	 * @param params
	 * @return
	 */
	List<WeChatDebtPlanBorrowCustomize> selectPlanBorrowList(Map<String, Object> params);

	/**
	 * 查询相应的计划的加入明细
	 * 
	 * @param params
	 * @return
	 */
	List<WeChatDebtPlanAccedeCustomize> selectPlanAccedeList(Map<String, Object> params);

	/**
	 * 查询相应的计划加入明细记录
	 * 
	 * @param params
	 * @return
	 */
	int countPlanAccedeRecordTotal(Map<String, Object> params);

	/**
	 * 查询相应的计划加入总额
	 * 
	 * @param params
	 * @return
	 */
	Long selectPlanAccedeSum(Map<String, Object> params);

	/**
	 * 加入计划后更新计划总信息
	 * 
	 * @param params
	 * @return
	 */
	int updateByDebtPlanId(Map<String, Object> plan);

	/**
	 * 加入计划后更新计划满标
	 * 
	 * @param params
	 * @return
	 */
	void updateOfFullPlan(Map<String, Object> planFull);

	/**
	 * 
	 * 查询计划清算详情
	 * 
	 * @author renxingchen
	 * @param planNid
	 * @return
	 */
	WeChatDebtPlanDetailCustomize selectPlanLanLiquidationDetail(String planNid);

	/**
	 * 
	 * @method: countPlanBorrowRecordTotalCredit
	 * @description: 包括债转的债权列表总数
	 * @param params
	 * @return
	 * @return: int
	 * @mender: zhouxiaoshuai
	 * @date: 2016年11月25日 下午3:48:17
	 */
	int countPlanBorrowRecordTotalCredit(Map<String, Object> params);

	/**
	 * 查询相应的计划标的列表
	 * 
	 * @param params
	 * @return
	 * @author Administrator
	 */
	List<WeChatDebtPlanBorrowCustomize> selectPlanBorrowListCredit(Map<String, Object> params);

	/**
	 * @method: selectLiquidateWill
	 * @description: 查询相应的明日将要清算的计划
	 * @return
	 * @return: List<DebtPlan>
	 * @mender: zhouxiaoshuai
	 * @date: 2016年12月12日 下午2:02:03
	 */
	List<DebtPlan> selectLiquidateWill();

	int countPlanBorrowRecordTotalLast(Map<String, Object> params);

	List<WeChatDebtPlanBorrowCustomize> selectPlanBorrowListLast(Map<String, Object> params);

	WeChatDebtPlanInvestInfoCustomize selectPlanInvestInfo(Map<String, Object> params);

}
