package com.hyjf.batch.htj.plancreditrepay;

import java.math.BigDecimal;
import java.util.List;

import com.hyjf.batch.BaseService;
import com.hyjf.mybatis.model.auto.Account;
import com.hyjf.mybatis.model.auto.AccountChinapnr;
import com.hyjf.mybatis.model.auto.DebtApicron;
import com.hyjf.mybatis.model.auto.DebtBorrowWithBLOBs;
import com.hyjf.mybatis.model.auto.DebtCredit;
import com.hyjf.mybatis.model.auto.DebtCreditRepay;
import com.hyjf.mybatis.model.auto.DebtCreditTender;
import com.hyjf.mybatis.model.auto.DebtInvest;
import com.hyjf.mybatis.model.auto.DebtLoan;
import com.hyjf.mybatis.model.auto.DebtLoanDetail;

public interface PlanCreditRepayService extends BaseService {

	/**
	 * 取得还款明细列表
	 *
	 * @return
	 */
	public List<DebtLoan> getBorrowRecoverList(String borrowNid);

	/**
	 * 取出账户信息
	 *
	 * @param userId
	 * @return
	 */
	public Account getAccountByUserId(Integer userId);

	/**
	 * 取得借款列表
	 *
	 * @return
	 */
	public DebtInvest getBorrowTender(Integer tenderId);

	/**
	 * 还款未债转部分
	 * 
	 * @param apicron
	 * @param borrowRecover
	 * @param borrowUserCust
	 * @return
	 * @throws Exception
	 */
	public boolean updateBorrowRepay(DebtApicron apicron, DebtLoan borrowRecover, AccountChinapnr borrowUserCust)
			throws Exception;

	/**
	 * 取得还款API任务表
	 *
	 * @return
	 */
	public List<DebtApicron> getBorrowApicronList(Integer status, Integer apiType, Integer creditStatus);

	/**
	 * 取得本期应还金额
	 *
	 * @return
	 */
	public BigDecimal getBorrowAccountWithPeriod(String borrowNid, String borrowStyle, Integer period);

	/**
	 * 更新借款API任务表
	 *
	 * @param id
	 * @param status
	 * @return
	 */
	public int updateBorrowApicron(Integer id, Integer status);

	/**
	 * 更新还款API任务表
	 *
	 * @return
	 */
	public int updateBorrowApicron(Integer id, Integer status, String data);

	/**
	 * 更新还款信息
	 *
	 * @param record
	 * @return
	 */
	public int updateBorrowRecover(DebtLoan recoder);

	/**
	 * 更新还款完成状态
	 *
	 * @param borrowNid
	 * @param borrow
	 * @param periodNow
	 */
	public void updateBorrowStatus(String borrowNid, Integer periodNow, Integer borrowUserId);

	/**
	 * 取得标的详情
	 * 
	 * @param borrowNid
	 * @return
	 */
	public DebtBorrowWithBLOBs getBorrow(String borrowNid);

	/**
	 * 查询相应的债权列表
	 * 
	 * @param borrowNid
	 * @param recoverPeriod
	 * @param status
	 * @return
	 */
	public List<DebtCredit> getBorrowCreditList(String borrowNid, int recoverPeriod, int status);

	/**
	 * 查询相应的债权数据
	 * 
	 * @param borrowNid
	 * @param investOrderId
	 * @param period
	 * @param status
	 * @return
	 */
	public List<DebtCredit> selectBorrowCreditList(String borrowNid, String investOrderId, int period, int status);

	/**
	 * 查询相应的债权出借数据
	 * 
	 * @param borrowNid
	 * @param investOrderId
	 * @param creditNid
	 * @return
	 */
	public List<DebtCreditTender> selectCreditTenderList(String borrowNid, String investOrderId, String creditNid);

	/**
	 * 查询相应的分期还款数据
	 * 
	 * @param borrowNid
	 * @param periodNow
	 * @param tenderUserId
	 * @param tenderId
	 * @return
	 */
	public DebtLoanDetail getBorrowRecoverPlan(String borrowNid, int periodNow, int tenderUserId, int tenderId);

	/**
	 * 更新相应的债权数据
	 * 
	 * @param creditNid
	 * @param repayPeriod
	 * @param creditStatus
	 * @param nowTime
	 * @param repayNextTime
	 * @return
	 */
	public boolean updateBorrowCredit(String creditNid, int repayPeriod, int creditStatus, int nowTime,
			int repayNextTime);

	/**
	 * 更新相应的债权数据
	 * 
	 * @param borrowCredit
	 * @return
	 */
	public int updateBorrowCredit(DebtCredit borrowCredit);

	/**
	 * 查询相应的债转承接记录
	 * 
	 * @param borrowNid
	 * @param assignUserId
	 * @param investOrderId
	 * @param assignNid
	 * @param periodNow
	 * @param i
	 * @return
	 */
	public DebtCreditRepay selectCreditRepay(String borrowNid, int assignUserId, String investOrderId, String assignNid,
			int periodNow, int i);

	/**
	 * 更新相应的债转还款计划
	 * 
	 * @param creditRepay
	 * @return
	 */
	public int updateCreditRepay(DebtCreditRepay creditRepay);

	/**
	 * 更新相应的出借人还款计划
	 * 
	 * @param borrowRecoverPlan
	 * @return
	 */
	public int updateBorrowRecoverPlan(DebtLoanDetail borrowRecoverPlan);

	/**
	 * 查询相应的债权数据
	 * 
	 * @param id
	 * @return
	 */
	public DebtCredit selectDebtCredit(Integer id);

	/**
	 * 债转承接部分还款
	 * 
	 * @param apicron
	 * @param borrow
	 * @param borrowUserCust
	 * @param debtLoan
	 * @param debtCredit
	 * @return
	 * @throws Exception
	 */
	public boolean creditRepay(DebtApicron apicron, DebtBorrowWithBLOBs borrow, AccountChinapnr borrowUserCust,
			DebtLoan debtLoan, DebtCredit debtCredit) throws Exception;

	/**
	 * 重新清算相应的债权
	 * 
	 * @param debtCredit
	 * @return
	 */
	public boolean updateCreditRecord(DebtCredit debtCredit, DebtBorrowWithBLOBs borrow, DebtApicron debtApicron);

	/**
	 * 承接中的还款
	 * 
	 * @param apicron
	 * @param borrow
	 * @param borrowUserCust
	 * @param debtLoan
	 * @param debtCredit
	 * @return
	 */
	public boolean creditAssignRepay(DebtApicron apicron, DebtBorrowWithBLOBs borrow, AccountChinapnr borrowUserCust,
			DebtLoan debtLoan, DebtCredit debtCredit);

	public boolean updateCreditLiquidates(DebtBorrowWithBLOBs borrow, DebtApicron apicron, DebtCredit debtCredit);

	public DebtLoan getBorrowRecover(Integer id);

}
