package com.hyjf.mybatis.mapper.customize.admin.htj;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.hyjf.mybatis.model.auto.DebtBorrow;
import com.hyjf.mybatis.model.customize.admin.htj.DebtBorrowCommonCustomize;
import com.hyjf.mybatis.model.customize.admin.htj.DebtBorrowCustomize;

public interface DebtBorrowCustomizeMapper {

	/**
	 * 获取借款列表
	 * 
	 * @param debtBorrowCommonCustomize
	 * @return
	 */
	List<DebtBorrowCustomize> selectBorrowList(DebtBorrowCommonCustomize debtBorrowCommonCustomize);

	/**
	 * COUNT
	 * 
	 * @param debtBorrowCommonCustomize
	 * @return
	 */
	Long countBorrow(DebtBorrowCommonCustomize debtBorrowCommonCustomize);

	/**
	 * 总额合计
	 * 
	 * @param debtBorrowCommonCustomize
	 * @return
	 */
	BigDecimal sumAccount(DebtBorrowCommonCustomize borrowCommonCustomize);

	/**
	 * 借款预编码
	 * 
	 * @param borrowCustomize
	 * @return
	 */
	String getBorrowPreNid(@Param("mmdd") String mmdd);

	/**
	 * 列表导出
	 * 
	 * @param borrowCustomize
	 * @return
	 */
	List<DebtBorrowCommonCustomize> exportBorrowList(DebtBorrowCommonCustomize debtBorrowCommonCustomize);

	/**
	 * 查询出到期但是未满标的标的,给其发短信
	 * 
	 * @return
	 */
	List<DebtBorrowCommonCustomize> searchNotFullBorrowMsg();

	/**
	 * @param updateOfLoansBorrow
	 * @return
	 */
	int updateOfBorrow(Map<String, Object> borrow);

	/**
	 * @param updateOfRepayTender
	 * @return
	 */
	int updateOfFullBorrow(Map<String, Object> borrow);

	/**
	 * @param updateOfBorrowAppoint
	 * @return
	 */
	int updateOfBorrowAppoint(Map<String, Object> borrow);

	/**
	 * @param updateOfFullBorrowAppoint
	 * @return
	 */
	int updateOfFullBorrowAppoint(Map<String, Object> borrowFull);

	/**
	 * @param updateOfLoansBorrow
	 * @return
	 */
	int updateCancelOfBorrowAppoint(Map<String, Object> borrow);

    List<DebtBorrowCustomize> selectDebtBorrowList(DebtBorrowCommonCustomize debtBorrowCommonCustomize);

    List<DebtBorrow> selectFullBorrow(String planNid);
}