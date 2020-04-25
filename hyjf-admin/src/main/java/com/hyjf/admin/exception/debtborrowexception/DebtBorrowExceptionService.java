package com.hyjf.admin.exception.debtborrowexception;

import java.math.BigDecimal;
import java.util.List;

import com.hyjf.mybatis.model.customize.admin.htj.DebtBorrowCommonCustomize;
import com.hyjf.mybatis.model.customize.admin.htj.DebtBorrowCustomize;
import com.hyjf.mybatis.model.customize.admin.htj.DebtBorrowExceptionDeleteBean;
import com.hyjf.mybatis.model.customize.admin.htj.DebtBorrowExceptionDeleteSrchBean;

public interface DebtBorrowExceptionService {

	/**
	 * 合计
	 * 
	 * @return
	 */
	public Long countBorrow(DebtBorrowCommonCustomize borrowCommonCustomize);

	/**
	 * 总额合计
	 * 
	 * @param borrowCustomize
	 * @return
	 */
	public BigDecimal sumAccount(DebtBorrowCommonCustomize borrowCommonCustomize);

	/**
	 * 借款列表
	 * 
	 * @return
	 */
	public List<DebtBorrowCustomize> selectBorrowList(DebtBorrowCommonCustomize borrowCommonCustomize);
	
	/**
	 * 根据bnid获取borrow信息
	 * 
	 * @return
	 */
	public List<DebtBorrowCustomize> selectBorrowByNid(String nid);
	
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
	public Long countBorrowDelete(DebtBorrowExceptionDeleteSrchBean form);
	
	/**
	 * 删除的borrow数据列表
	 * 
	 * @return
	 */
	public List<DebtBorrowExceptionDeleteBean> selectBorrowDeleteList(DebtBorrowExceptionDeleteSrchBean form);
}
