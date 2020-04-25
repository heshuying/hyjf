package com.hyjf.admin.manager.debt.debtborrowfirst;

import java.util.List;

import com.hyjf.admin.BaseService;
import com.hyjf.mybatis.model.customize.admin.htj.DebtBorrowCommonCustomize;
import com.hyjf.mybatis.model.customize.admin.htj.DebtBorrowFirstCustomize;

public interface DebtBorrowFirstService extends BaseService {

	/**
	 * 数据合计
	 * 
	 * @return
	 */
	public Integer countBorrowFirst(DebtBorrowCommonCustomize debtBorrowCommonCustomize);

	/**
	 * 获取列表
	 * 
	 * @return
	 */
	public List<DebtBorrowFirstCustomize> selectBorrowFirstList(DebtBorrowCommonCustomize debtBorrowCommonCustomize);

	/**
	 * 交保证金
	 * 
	 * @param record
	 */
	public void borrowBailRecord(String borrowPreNid);

	/**
	 * 已交保证金
	 * 
	 * @param borrow
	 * @return
	 * @author Administrator
	 */
	public boolean getBorrowBail(String borrowNid);

	/**
	 * 更新
	 * 
	 * @param record
	 */
	public void updateOntimeRecord(DebtBorrowFirstBean borrowBean);

	/**
	 * 获取用户名
	 * 
	 * @param record
	 */
	public String getUserName(Integer userId);

	/**
	 * 统计总额
	 * @param debtBorrowCommonCustomize
	 * @return
	 */
	public String sumBorrowFirstAccount(DebtBorrowCommonCustomize debtBorrowCommonCustomize);
	
	/**
	 * 根据订单号,查询其是否有预约记录
	 * @param corrowCommonCustomize
	 * @return
	 */
	public Boolean hasBookingRecord(String nid);
}
