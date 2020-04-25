package com.hyjf.mybatis.mapper.customize;

import java.util.List;

import com.hyjf.mybatis.model.customize.BillionSecondCustomize;

public interface BillionSecondCustomizeMapper {

	/**
	 * 获取用户出借金额
	 * 
	 * @param BillionSecondCustomize
	 * @return
	 */
	List<BillionSecondCustomize> selectUserTenderList(BillionSecondCustomize billionSecondCustomize);

}