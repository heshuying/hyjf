package com.hyjf.mybatis.mapper.customize.admin.htj;

import java.math.BigDecimal;
import java.util.List;

import com.hyjf.mybatis.model.customize.admin.htj.DebtBorrowCommonCustomize;
import com.hyjf.mybatis.model.customize.admin.htj.DebtBorrowCustomize;
import com.hyjf.mybatis.model.customize.admin.htj.DebtBorrowExceptionDeleteBean;
import com.hyjf.mybatis.model.customize.admin.htj.DebtBorrowExceptionDeleteSrchBean;

public interface DebtAdminBorrowExceptionMapper {

	/**
	 * 获取借款列表
	 * 
	 * @param alllBorrowCustomize
	 * @return
	 */
	List<DebtBorrowCustomize> selectBorrowList(DebtBorrowCommonCustomize borrowCommonCustomize);

	/**
	 * COUNT
	 * 
	 * @param borrowCustomize
	 * @return
	 */
	Long countBorrow(DebtBorrowCommonCustomize borrowCommonCustomize);

	/**
	 * 总额合计
	 * 
	 * @param borrowCustomize
	 * @return
	 */
	BigDecimal sumAccount(DebtBorrowCommonCustomize borrowCommonCustomize);

	/**
	 * 根据bnid获取borrow信息
	 * 
	 * @param nid
	 * @return
	 * @author zhuxiaodong
	 */
	List<DebtBorrowCustomize> selectBorrowByNid(String nid);

	/**
	 * 保存删除的borrow信息
	 * 
	 * @param nid
	 * @return
	 * @author zhuxiaodong
	 */
	int insert(DebtBorrowExceptionDeleteBean borrowExceptionDeleteBean);

	/**
	 * 删除的COUNT
	 * 
	 * @param borrowCustomize
	 * @return
	 */
	Long countBorrowDelete(DebtBorrowExceptionDeleteSrchBean borrowExceptionDelete);

	/**
	 * 删除的borrow数据列表
	 * 
	 * @param DebtBorrowExceptionDeleteBean
	 * @return
	 * @author zhuxiaodong
	 */
	List<DebtBorrowExceptionDeleteBean> selectBorrowDeleteList(DebtBorrowExceptionDeleteSrchBean form);
}