package com.hyjf.mybatis.mapper.customize.admin;

import java.util.List;

import com.hyjf.mybatis.model.customize.admin.AdminTenderExceptionCustomize;

public interface AdminTenderExceptionCustomizeMapper {

	/**
	 * 出借明细列表
	 * 
	 * @param alllBorrowCustomize
	 * @return
	 */
	List<AdminTenderExceptionCustomize> selectTenderExceptionList(AdminTenderExceptionCustomize record);

	/**
	 * 出借明细记录 总数COUNT
	 * 
	 * @param borrowCustomize
	 * @return
	 */
	Integer countTenderException(AdminTenderExceptionCustomize record);

	/**
	 * 导出出借明细列表
	 * 
	 * @param alllBorrowCustomize
	 * @return
	 */
	List<AdminTenderExceptionCustomize> exportTenderExceptionList(AdminTenderExceptionCustomize record);

	/**
	 * 出借金额合计
	 * 
	 * @param borrowCustomize
	 * @return
	 */
	String sumTenderExceptionAccount(AdminTenderExceptionCustomize record);

}