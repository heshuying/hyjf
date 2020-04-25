/**
 * Description:会员用户开户记录初始化列表查询
 * Copyright: (HYJF Corporation) 2015
 * Company: HYJF Corporation
 * @author: 王坤
 * @version: 1.0
 *           Created at: 2015年11月11日 上午11:01:57
 *           Modification History:
 *           Modified by :
 * */

package com.hyjf.mybatis.mapper.customize.web.htj;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.hyjf.mybatis.model.auto.DebtPlan;
import com.hyjf.mybatis.model.customize.web.htj.DebtPlanAccedeCustomize;
import com.hyjf.mybatis.model.customize.web.htj.DebtPlanBorrowCustomize;
import com.hyjf.mybatis.model.customize.web.htj.DebtPlanCustomize;
import com.hyjf.mybatis.model.customize.web.htj.DebtPlanDetailCustomize;
import com.hyjf.mybatis.model.customize.web.htj.DebtPlanIntroduceCustomize;
import com.hyjf.mybatis.model.customize.web.htj.DebtPlanInvestDataCustomize;
import com.hyjf.mybatis.model.customize.web.htj.DebtPlanQuestionCustomize;
import com.hyjf.mybatis.model.customize.web.htj.DebtPlanRiskControlCustomize;

public interface DebtPlanCustomizeMapper {

	/**
	 * 查询相应的计划列表信息
	 * 
	 * @param params
	 * @return
	 */
	List<DebtPlanCustomize> selectDebtPlanList(Map<String, Object> params);

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
	DebtPlanDetailCustomize selectDebtPlanDetail(@Param("planNid") String planNid);

	/***
	 * 查询相应的计划预览
	 * 
	 * @param borrowNid
	 * @return
	 */
	DebtPlanDetailCustomize selectDebtPlanPreview(@Param("planNid") String planNid);

	/**
	 * 查询相应的计划介绍
	 * 
	 * @param planNid
	 * @return
	 */

	DebtPlanIntroduceCustomize selectDebtPlanIntroduce(@Param("planNid") String planNid);

	/**
	 * 查询相应的计划的风控信息
	 * 
	 * @param planNid
	 * @return
	 */

	DebtPlanRiskControlCustomize selectDebtPlanRiskControl(@Param("planNid") String planNid);

	/**
	 * 查询相应的计划问题
	 * 
	 * @param planNid
	 * @return
	 */

	DebtPlanQuestionCustomize selectDebtPlanQuestion(@Param("planNid") String planNid);

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

	List<DebtPlanBorrowCustomize> selectPlanBorrowList(Map<String, Object> params);

	/**
	 * 查询相应的计划的加入明细
	 * 
	 * @param params
	 * @return
	 */
	List<DebtPlanAccedeCustomize> selectPlanAccedeList(Map<String, Object> params);

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
	DebtPlanDetailCustomize selectPlanLanLiquidationDetail(String planNid);

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
	List<DebtPlanBorrowCustomize> selectPlanBorrowListCredit(Map<String, Object> params);

	/**
	 * 
	 * @method: selectLiquidateWill
	 * @description: 查询相应的明日将要清算的计划
	 * @return
	 * @return: List<DebtPlan>
	 * @mender: zhouxiaoshuai
	 * @date: 2016年12月12日 下午2:02:03
	 */
	List<DebtPlan> selectLiquidateWill();

	int countPlanBorrowRecordTotalLast(Map<String, Object> params);

	List<DebtPlanBorrowCustomize> selectPlanBorrowListLast(Map<String, Object> params);

	/**
	 * 查询汇添金出借数据
	 * 
	 * @return
	 */
	DebtPlanInvestDataCustomize searchInvestData();

}
