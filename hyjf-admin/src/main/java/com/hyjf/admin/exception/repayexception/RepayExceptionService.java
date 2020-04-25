package com.hyjf.admin.exception.repayexception;

import java.util.List;

import com.hyjf.admin.BaseService;
import com.hyjf.mybatis.model.auto.Borrow;
import com.hyjf.mybatis.model.auto.BorrowRecover;
import com.hyjf.mybatis.model.auto.BorrowRecoverPlan;
import com.hyjf.mybatis.model.customize.RepayExceptionCustomize;

public interface RepayExceptionService extends BaseService {

    /**
     * 出借明细列表
     *
     * @param alllBorrowCustomize
     * @return
     */
    public List<RepayExceptionCustomize> selectBorrowRepaymentList(RepayExceptionCustomize repayExceptionCustomize);

    /**
     * 出借明细记录 总数COUNT
     *
     * @param borrowCustomize
     * @return
     */
    public Long countBorrowRepayment(RepayExceptionCustomize repayExceptionCustomize);

    /**
     * 重新还款
     *
     * @param record
     */
    public void updateBorrowApicronRecord(String borrowNid, Integer periodNow);

    /**
     * 手动还款
     *
     * @param record
     */
    public void updateRrepayTemp(String borrowNid, Integer periodNow);

    /**
     * 进行单笔还款
     * @param orderId
     * @param period
     * @return
     * @throws Exception 
     */
	public boolean updateBorrowRepay(String orderId, String period) throws Exception;

	/**
	 * 根据订单号查询相应的用户还款总表
	 * @param orderId
	 * @return
	 */
	public BorrowRecover selectBorrowRecoverByNid(String orderId);

	/**
	 * 根据标号获取标信息
	 * @param borrowNid
	 * @return
	 */
	public Borrow selectBorrow(String borrowNid);

	/**
	 * 根据订单号，期数查询相应的还款分期表
	 * @param orderId
	 * @param parseInt
	 * @return
	 */
	public BorrowRecoverPlan selectBorrowRecoverPlan(String orderId, int parseInt);

}
