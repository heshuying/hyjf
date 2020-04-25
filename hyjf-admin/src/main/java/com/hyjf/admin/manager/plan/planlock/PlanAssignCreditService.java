package com.hyjf.admin.manager.plan.planlock;

import java.math.BigDecimal;

import com.hyjf.admin.BaseService;
import com.hyjf.mybatis.model.auto.DebtBorrow;
import com.hyjf.mybatis.model.auto.DebtCredit;
import com.hyjf.mybatis.model.auto.DebtFreeze;
import com.hyjf.mybatis.model.auto.DebtPlanAccede;

/**
 * 计划转让类产品认购Service
 * 
 * @ClassName PlanCreditTenderService
 * @author 刘阳
 * @date 2016年11月9日 下午2:52:39
 */
public interface PlanAssignCreditService extends BaseService {
	/**
	 * 承接债权
	 * 
	 * @param debtCreditMin
	 * @param debtPlanAccede
	 * @param accedeBalance
	 * @param lockPeintriod
	 * @param expectApr
	 * @return
	 */
	public boolean assignCredit(DebtCredit debtCredit, DebtPlanAccede assignDebtPlanAccede, BigDecimal account, BigDecimal expectApr, BigDecimal minSurplusInvestAccount) ;

	/**
	 * 根据计划nid，计划加入订单号，查询相应的冻结记录
	 * 
	 * @param planNid
	 * @param planOrderId
	 * @return
	 */

	public DebtFreeze selectDebtFreeze(String planNid, String planOrderId);


	/**
	 * 根据项目编号查询项目的可出借金额
	 * 
	 * @param borrowNid
	 * @return
	 */
	DebtBorrow selectDebtBorrowByNid(String borrowNid);

	/**
	 * 更新用户的计划加入订单为完成
	 * 
	 * @param debtPlanAccede
	 * @return
	 */
	boolean updateDebtPlanAccedeFinish(DebtPlanAccede debtPlanAccede);

	/**
	 * 根据id获取计划加入记录
	 * 
	 * @param id
	 * @return
	 */
	DebtPlanAccede selectDebtPlanAccede(Integer id);

	/**
	 * 根据债转标号检索债转
	 * 
	 * @Title selectCreditListByCreditNid
	 * @param creditNid
	 * @return
	 */
	public DebtCredit selectCreditListByCreditNid(String creditNid);

}
