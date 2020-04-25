package com.hyjf.admin.manager.hjhplan.planrepay;

import java.util.List;
import java.util.Map;

import com.hyjf.mybatis.model.auto.HjhPlan;
import com.hyjf.mybatis.model.auto.HjhRepay;
import com.hyjf.mybatis.model.auto.Users;
import com.hyjf.mybatis.model.customize.BorrowRepaymentCustomize;
import com.hyjf.mybatis.model.customize.HjhCreditTenderCustomize;
import com.hyjf.mybatis.model.customize.PlanCommonCustomize;
import com.hyjf.mybatis.model.customize.web.hjh.HjhPlanRepayCustomize;

public interface PlanRepayListService {
	
	/**
	 * 计划列表查询件数
	 * 
	 * @method: countPlan
	 * @return: int
	 * @mender: LIBIN
	 * @date: 2017年8月11日
	 */
	 int countRepayPlan(PlanCommonCustomize planCommonCustomize);
	 
	/**
	 * 计划列表查询
	 * 
	 * @method: selectPlanList
	 * @return: List
	 * @mender: LIBIN
	 * @date: 2017年8月11日
	 */
	 List<HjhRepay> selectPlanList(PlanCommonCustomize planCommonCustomize);
	 
	/**
	 * 还款信息记录 总数COUNT
	 *
	 * @param borrowCustomize
	 * @return
	 */
	Long countBorrowRepayment(BorrowRepaymentCustomize borrowRepaymentCustomize);
	
	/**
	 * 还款信息列表
	 *
	 * @param borrowCommonCustomize
	 * @return
	 */
	List<BorrowRepaymentCustomize> selectBorrowRepaymentList(BorrowRepaymentCustomize borrowCommonCustomize);

	List<HjhPlanRepayCustomize> exportPlanRepayList(Map<String,Object> paraMap);
	
	/**
	* 根据用户ID取得用户信息
	*
	* @param userId
	* @return
	*/
	public Users getUsersByUserId(Integer userId);
	/**
	 * 根据计划编号获取计划信息
	 *
	 * @param planNid
	 * @return
	 */
	public HjhPlan getPlan(String planNid);
	
	/**
	 * (债转)还款信息记录 总数COUNT
	 *
	 * @param borrowCustomize
	 * @return
	 */
	Long countCreditBorrowRepayment(BorrowRepaymentCustomize borrowRepaymentCustomize);
	
	Long countCreditBorrowTender(HjhCreditTenderCustomize borrowRepaymentCustomize);
	
	/**
	 * (债转)还款信息列表
	 *
	 * @param borrowCommonCustomize
	 * @return
	 */
	List<BorrowRepaymentCustomize> selectCreditBorrowRepaymentList(BorrowRepaymentCustomize borrowCommonCustomize);
	List<HjhCreditTenderCustomize> selectCreditBorrowTendertList(HjhCreditTenderCustomize borrowCommonCustomize);
	
	/**
	* 获取金额合计值
	*
	* @param planCommonCustomize
	* @return
	*/
	public PlanCommonCustomize sumHjhRepay(PlanCommonCustomize planCommonCustomize);	
	
	/**
	 * 金额合计值获取
	 *
	 * @param borrowCommonCustomize
	 * @return
	 */
	BorrowRepaymentCustomize sumBorrowRepayment(BorrowRepaymentCustomize borrowRepaymentCustomize);
}
