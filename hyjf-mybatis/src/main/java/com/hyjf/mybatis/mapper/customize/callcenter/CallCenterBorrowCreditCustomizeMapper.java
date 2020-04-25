package com.hyjf.mybatis.mapper.customize.callcenter;

import java.util.List;

import com.hyjf.mybatis.model.customize.callcenter.CallCenterBorrowCreditCustomize;

public interface CallCenterBorrowCreditCustomizeMapper {

	/**
	 * 获取列表
	 * 
	 * @param borrowCreditCustomize
	 * @return
	 */
	List<CallCenterBorrowCreditCustomize> selectBorrowCreditList(CallCenterBorrowCreditCustomize callCenterBorrowCreditCustomize);
	/**
	 * 获取详细列表
	 * 
	 * @param borrowCreditCustomize
	 * @return
	 */
	List<CallCenterBorrowCreditCustomize> selectBorrowCreditInfoList(CallCenterBorrowCreditCustomize borrowCreditCustomize);
}