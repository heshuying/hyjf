package com.hyjf.mybatis.mapper.customize.admin;

import java.math.BigDecimal;
import java.util.List;

import com.hyjf.mybatis.model.customize.BorrowCommonCustomize;
import com.hyjf.mybatis.model.customize.BorrowCustomize;
import com.hyjf.mybatis.model.customize.BorrowExceptionDeleteBean;
import com.hyjf.mybatis.model.customize.BorrowExceptionDeleteSrchBean;

public interface AdminBorrowExceptionMapper {

	/**
	 * 获取借款列表
	 * 
	 * @param alllBorrowCustomize
	 * @return
	 */
	List<BorrowCustomize> selectBorrowList(BorrowCommonCustomize borrowCommonCustomize);

	/**
	 * COUNT
	 * 
	 * @param borrowCustomize
	 * @return
	 */
	Long countBorrow(BorrowCommonCustomize borrowCommonCustomize);

	/**
	 * 总额合计
	 * 
	 * @param borrowCustomize
	 * @return
	 */
	BigDecimal sumAccount(BorrowCommonCustomize borrowCommonCustomize);
	
	/**
	 * 根据bnid获取borrow信息
	 * @param nid
	 * @return
	 * @author zhuxiaodong
	 */
	List<BorrowCustomize> selectBorrowByNid(String nid);
	
	/**
	 * 保存删除的borrow信息
	 * @param nid
	 * @return
	 * @author zhuxiaodong
	 */
	int insert(BorrowExceptionDeleteBean borrowExceptionDeleteBean);

	
	/**
	 * 删除的COUNT
	 * 
	 * @param borrowCustomize
	 * @return
	 */
	Long countBorrowDelete(BorrowExceptionDeleteSrchBean borrowExceptionDelete);
	
	
	/**
	 * 删除的borrow数据列表
	 * @param BorrowExceptionDeleteBean
	 * @return
	 * @author zhuxiaodong
	 */
	List<BorrowExceptionDeleteBean> selectBorrowDeleteList(BorrowExceptionDeleteSrchBean form);
}