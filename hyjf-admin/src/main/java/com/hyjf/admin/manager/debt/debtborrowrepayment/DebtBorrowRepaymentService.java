package com.hyjf.admin.manager.debt.debtborrowrepayment;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.List;

import com.hyjf.admin.BaseService;
import com.hyjf.mybatis.model.auto.Account;
import com.hyjf.mybatis.model.auto.AccountChinapnr;
import com.hyjf.mybatis.model.customize.admin.htj.DebtAdminRepayDelayCustomize;
import com.hyjf.mybatis.model.customize.admin.htj.DebtBorrowRepaymentCustomize;

public interface DebtBorrowRepaymentService extends BaseService {

	/**
	 * 出借明细列表
	 *
	 * @param alllBorrowCustomize
	 * @return
	 */
	public List<DebtBorrowRepaymentCustomize> selectBorrowRepaymentList(DebtBorrowRepaymentCustomize borrowCommonCustomize);

	/**
	 * 出借明细记录 总数COUNT
	 *
	 * @param borrowCustomize
	 * @return
	 */
	public Long countBorrowRepayment(DebtBorrowRepaymentCustomize borrowRepaymentCustomize);

	/**
	 * 统计合计
	 * 
	 * @param borrowRepaymentCustomize
	 * @return
	 */
	public DebtBorrowRepaymentCustomize sumBorrowRepaymentInfo(DebtBorrowRepaymentCustomize borrowRepaymentCustomize);

	/**
	 * 延期画面初始化
	 *
	 * @param borrowNid
	 * @return
	 */
	public DebtAdminRepayDelayCustomize selectBorrowInfo(String borrowNid);

	/**
	 * 添加延期时间
	 *
	 * @param borrowNid
	 * @param afterDay
	 * @throws ParseException
	 */
	public void updateBorrowRepayDelayDays(String borrowNid, String afterDay) throws ParseException;

	/**
	 * 用户延期时获取相应的单期延期数据
	 * 
	 * @param borrowNid
	 * @return
	 * @throws ParseException
	 */
	public DebtBorrowRepayBean getBorrowRepayDelay(String borrowNid, String borrowApr, String borrowStyle) throws ParseException;

	/**
	 * 用户延期时获取相应的多期中一期的还款数据
	 * 
	 * @param borrowNid
	 * @return
	 * @throws ParseException
	 */
	public DebtBorrowRepayPlanBean getBorrowRepayPlanDelay(String borrowNid, String borrowApr, String borrowStyle) throws ParseException;

	/**
	 * 用户还款时获取相应的单期还款信息
	 * 
	 * @param borrowNid
	 * @param borrowStyle
	 * @param string
	 * @return
	 * @throws ParseException
	 */
	public DebtBorrowRepayBean getBorrowRepayInfo(String borrowNid, String borrowApr, String borrowStyle) throws ParseException;

	/**
	 * 用户还款时获取相应的多期中一期的还款数据
	 * 
	 * @param borrowNid
	 * @param borrowStyle
	 * @return
	 * @throws ParseException
	 */
	public DebtBorrowRepayPlanBean getBorrowRepayPlanInfo(String borrowNid, String borrowApr, String borrowStyle) throws ParseException;

	/**
	 * 根据userId获取相应的用户账户余额信息
	 * 
	 * @param userId
	 * @return
	 */
	public Account getUserAccount(String userId);

	public void updateBorrowRecover(DebtBorrowRepayBean borrowRepay);

	public void updateBorrowRecoverPlan(DebtBorrowRepayPlanBean borrowRepayPlan);

	/**
	 * 重新还款
	 *
	 * @param record
	 */
	public void updateBorrowApicronRecord(String borrowNid);

	/**
	 * 计算管理费
	 *
	 * @return
	 */
	public int incomeFeeService() throws Exception;

	public AccountChinapnr getChinapnrUserInfo(Integer userId);

	public BigDecimal getUserBalance(Long chinapnrUsrcustid);
}
