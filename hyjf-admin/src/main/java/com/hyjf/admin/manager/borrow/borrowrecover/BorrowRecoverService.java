package com.hyjf.admin.manager.borrow.borrowrecover;

import java.util.List;

import com.hyjf.admin.BaseService;
import com.hyjf.mybatis.model.auto.BorrowRecover;
import com.hyjf.mybatis.model.auto.BorrowRecoverPlan;
import com.hyjf.mybatis.model.customize.BorrowRecoverCustomize;

public interface BorrowRecoverService extends BaseService {

	/**
	 * 出借明细列表
	 * 
	 * @param alllBorrowCustomize
	 * @return
	 */
	public List<BorrowRecoverCustomize> selectBorrowRecoverList(BorrowRecoverCustomize borrowRecoverCustomize);

	/**
	 * 放款明细记录 合计
	 * 
	 * @param borrowRecoverCustomize
	 * @return
	 */
	public BorrowRecoverCustomize sumBorrowRecoverList(BorrowRecoverCustomize borrowRecoverCustomize);

	/**
	 * 出借明细记录 总数COUNT
	 * 
	 * @param borrowCustomize
	 * @return
	 */
	public Long countBorrowRecover(BorrowRecoverCustomize borrowRecoverCustomize);

	/**
	 * 导出明细列表
	 * 
	 * @param alllBorrowCustomize
	 * @return
	 */
	public List<BorrowRecoverCustomize> selectExportBorrowRecoverList(BorrowRecoverCustomize borrowRecoverCustomize);
	
	/**
	 * 垫付协议-放款记录（出借人）
	 */
	public List<BorrowRecover> selectBorrowRecover(String borrowNid);
	
	/**
     * 垫付协议-分期放款记录（出借人）
     */
	public List<BorrowRecoverPlan> selectBorrowRecoverPlan(String borrowNid,int repayPeriod);
	public List<BorrowRecoverPlan> selectBorrowRecoverPlanByNid(String nid,int repayPeriod);
}
