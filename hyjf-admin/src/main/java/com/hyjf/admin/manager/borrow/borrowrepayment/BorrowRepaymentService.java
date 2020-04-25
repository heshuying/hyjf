package com.hyjf.admin.manager.borrow.borrowrepayment;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.List;

import com.hyjf.admin.BaseService;
import com.hyjf.mybatis.model.auto.Account;
import com.hyjf.mybatis.model.auto.AccountChinapnr;
import com.hyjf.mybatis.model.customize.BorrowRepaymentCustomize;
import com.hyjf.mybatis.model.customize.admin.AdminRepayDelayCustomize;

public interface BorrowRepaymentService extends BaseService {

	/**
	 * 出借明细列表
	 *
	 * @param alllBorrowCustomize
	 * @return
	 */
	public List<BorrowRepaymentCustomize> selectBorrowRepaymentList(BorrowRepaymentCustomize borrowCommonCustomize);
	
	/**
	 * 出借明细记录 总数COUNT
	 *
	 * @param borrowCustomize
	 * @return
	 */
	public Long countBorrowRepayment(BorrowRepaymentCustomize borrowRepaymentCustomize);
	/**
	 * 统计合计
	 * @param borrowRepaymentCustomize
	 * @return
	 */
	public BorrowRepaymentCustomize sumBorrowRepaymentInfo(BorrowRepaymentCustomize borrowRepaymentCustomize);

	/**
	 * 延期画面初始化
	 *
	 * @param borrowNid
	 * @return
	 */
	public AdminRepayDelayCustomize selectBorrowInfo(String borrowNid);


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
	 * @param borrowNid
	 * @return
	 * @throws ParseException
	 */
	public BorrowRepayBean getBorrowRepayDelay(String borrowNid,String borrowApr, String borrowStyle) throws ParseException;

	/**
	 * 用户延期时获取相应的多期中一期的还款数据
	 * @param borrowNid
	 * @return
	 * @throws ParseException
	 */
	public BorrowRepayPlanBean getBorrowRepayPlanDelay(String borrowNid,String borrowApr, String borrowStyle) throws ParseException;

	/**
	 * 用户还款时获取相应的单期还款信息
	 * @param borrowNid
	 * @param borrowStyle
	 * @param string
	 * @return
	 * @throws ParseException
	 */
	public BorrowRepayBean getBorrowRepayInfo(String borrowNid, String borrowApr, String borrowStyle) throws ParseException;
	/**
	 * 用户还款时获取相应的多期中一期的还款数据
	 * @param borrowNid
	 * @param borrowStyle
	 * @return
	 * @throws ParseException
	 */
	public BorrowRepayPlanBean getBorrowRepayPlanInfo(String borrowNid,String borrowApr, String borrowStyle) throws ParseException;


	/**
	 * 根据userId获取相应的用户账户余额信息
	 * @param userId
	 * @return
	 */
	public Account getUserAccount(String userId);

	public void updateBorrowRecover(BorrowRepayBean borrowRepay);

	public void updateBorrowRecoverPlan(BorrowRepayPlanBean borrowRepayPlan);

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
