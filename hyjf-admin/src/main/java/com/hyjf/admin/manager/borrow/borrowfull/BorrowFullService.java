package com.hyjf.admin.manager.borrow.borrowfull;

import java.util.List;

import com.hyjf.mybatis.model.customize.BorrowCommonCustomize;
import com.hyjf.mybatis.model.customize.BorrowFullCustomize;

public interface BorrowFullService {

	/**
	 * 复审记录 总数COUNT
	 * 
	 * @param borrowCustomize
	 * @return
	 */
	public Long countBorrowFull(BorrowCommonCustomize borrowCommonCustomize);

	/**
	 * 复审记录
	 * 
	 * @param borrowCustomize
	 * @return
	 */
	public List<BorrowFullCustomize> selectBorrowFullList(BorrowCommonCustomize borrowCommonCustomize);

	/**
	 * 获取复审状态
	 * 
	 * @param record
	 */
	public boolean isBorrowStatus16(BorrowFullBean borrowBean);

	/**
	 * 更新复审
	 * @param msg 
	 * 
	 * @param record
	 * @return 
	 */
	public boolean updateReverifyRecord(BorrowFullBean borrowBean, String msg);

	/**
	 * 流标
	 * 
	 * @param record
	 */
	public void updateBorrowRecordOver(BorrowFullBean borrowBean);

	/**
	 * 复审中的列表
	 * 
	 * @param borrowCustomize
	 * @return
	 */
	public List<BorrowFullCustomize> selectFullList(String borrowNid, int limitStart, int limitEnd);

	/**
	 * 复审详细
	 * 
	 * @param borrowFullCustomize
	 * @return
	 */
	public BorrowFullCustomize selectFullInfo(String borrowNid);

	/**
	 * 合计
	 * 
	 * @param borrowFullCustomize
	 * @return
	 */
	public BorrowFullCustomize sumAmount(String borrowNid);

	/**
	 * 重新放款
	 * 
	 * @param record
	 * @return 
	 */
	public boolean updateBorrowApicronRecord(BorrowFullBean borrowBean);

	/**
	 * 金额合计
	 * 
	 * @param record
	 * @return 
	 */
	public BorrowFullCustomize sumAccount(BorrowCommonCustomize borrowCommonCustomize);
}
