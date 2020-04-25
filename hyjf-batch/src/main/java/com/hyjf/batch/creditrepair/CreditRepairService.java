package com.hyjf.batch.creditrepair;

import java.util.List;

import com.hyjf.batch.BaseService;
import com.hyjf.mybatis.model.auto.BorrowWithBLOBs;
import com.hyjf.mybatis.model.auto.CreditRepay;
import com.hyjf.mybatis.model.auto.CreditTender;

public interface CreditRepairService extends BaseService {

	/**
	 * 获取未还款的债转还款数据
	 * 
	 * @return
	 */

	List<CreditRepay> selectCreditRepayList();

	/**
	 * 修复债转还款数据
	 * 
	 * @param creditRepay
	 */

	int updateCreditRepay(CreditRepay creditRepay);
	
	/**
	 * 取得标的详情
	 *
	 * @return
	 */
	public BorrowWithBLOBs selectBorrow(String borrowNid);

	/**
	 * 查询用户的债转承接记录
	 * @return
	 */
		
	CreditTender selectCreditTender(String assignNid);

}
