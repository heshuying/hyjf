package com.hyjf.admin.exception.borrowexception;

import java.math.BigDecimal;
import java.util.List;

import com.hyjf.mybatis.model.customize.BorrowCommonCustomize;
import com.hyjf.mybatis.model.customize.BorrowCustomize;
import com.hyjf.mybatis.model.customize.BorrowExceptionDeleteBean;
import com.hyjf.mybatis.model.customize.BorrowExceptionDeleteSrchBean;

public interface BorrowExceptionService {

	/**
	 * 合计
	 * 
	 * @return
	 */
	public Long countBorrow(BorrowCommonCustomize borrowCommonCustomize);

	/**
	 * 总额合计
	 * 
	 * @param borrowCustomize
	 * @return
	 */
	public BigDecimal sumAccount(BorrowCommonCustomize borrowCommonCustomize);

	/**
	 * 借款列表
	 * 
	 * @return
	 */
	public List<BorrowCustomize> selectBorrowList(BorrowCommonCustomize borrowCommonCustomize);

	/**
	 * 根据bnid获取borrow信息
	 * 
	 * @return
	 */
	public List<BorrowCustomize> selectBorrowByNid(String nid);

	/**
	 * 根据bnid删除borrow信息
	 * 
	 * @return
	 */
	public void deleteBorrowByNid(String nid);

	/**
	 * 删除的borrow数据合计
	 * 
	 * @return
	 */
	public Long countBorrowDelete(BorrowExceptionDeleteSrchBean form);

	/**
	 * 删除的borrow数据列表
	 * 
	 * @return
	 */
	public List<BorrowExceptionDeleteBean> selectBorrowDeleteList(BorrowExceptionDeleteSrchBean form);

	/**
	 * 标的撤销
	 * 
	 * @param borrowNid
	 */
	public void updateBorrowByNid(String borrowNid) throws Exception;
}
