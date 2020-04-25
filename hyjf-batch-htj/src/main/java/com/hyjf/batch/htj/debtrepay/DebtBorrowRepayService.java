package com.hyjf.batch.htj.debtrepay;

import java.math.BigDecimal;
import java.util.List;

import com.hyjf.batch.BaseService;
import com.hyjf.mybatis.model.auto.Account;
import com.hyjf.mybatis.model.auto.AccountChinapnr;
import com.hyjf.mybatis.model.auto.DebtApicron;
import com.hyjf.mybatis.model.auto.DebtBorrowWithBLOBs;
import com.hyjf.mybatis.model.auto.DebtInvest;
import com.hyjf.mybatis.model.auto.DebtLoan;
import com.hyjf.mybatis.model.auto.DebtLoanDetail;
import com.hyjf.mybatis.model.customize.coupon.CouponTenderCustomize;

public interface DebtBorrowRepayService extends BaseService {

	/**
	 * 取得还款明细列表
	 *
	 * @return
	 */
	public List<DebtLoan> getBorrowRecoverList(String borrowNid);

	/**
	 * 取得该标的下优惠券出借列表
	 *
	 * @return
	 */
	public List<CouponTenderCustomize> getCouponTenderList(String borrowNid);

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
	 * 自动放款(本金)
	 *
	 * @param apicron
	 * @return
	 */
	public boolean updateBorrowRepay(DebtApicron apicron, DebtLoan borrowRecover, AccountChinapnr borrowUserCust)
			throws Exception;

	/**
	 * 取得还款API任务表
	 *
	 * @return
	 */
	public List<DebtApicron> getBorrowApicronList(Integer status, Integer apiType);

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
	 * @return
	 */
	public DebtBorrowWithBLOBs getBorrow(String borrowNid);

	/**
	 * 还款后,解冻冻结处理
	 * 
	 * @Title repayBackUpdate
	 * @param userId
	 * @param userName
	 * @param planNid
	 * @param planOrderId
	 * @return
	 */
	public boolean repayBackUpdate(Integer userId, String userName, String planNid, String planOrderId)
			throws Exception;

	/**
	 * 更新相应的放款明细表
	 * 
	 * @param borrowRecoverPlan
	 * @return
	 */
	public int updateBorrowRecoverPlan(DebtLoanDetail borrowRecoverPlan);

	/**
	 * 查询相应的放款明细表
	 * 
	 * @param borrowNid
	 * @param periodNow
	 * @param tenderUserId
	 * @param investId
	 * @return
	 */
	public DebtLoanDetail getBorrowRecoverPlan(String borrowNid, Integer periodNow, Integer tenderUserId,
			Integer investId);

}
