package com.hyjf.admin.exception.borrowregistexception;

import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.mybatis.model.customize.BorrowCommonCustomize;
import com.hyjf.mybatis.model.customize.admin.BorrowRegistCustomize;

public interface BorrowRegistExceptionService {

	/**
	 * 合计
	 * 
	 * @return
	 */
	public Long countBorrow(BorrowCommonCustomize borrowCommonCustomize);

	/**
	 * 借款列表
	 * 
	 * @return
	 */
	public List<BorrowRegistCustomize> selectBorrowList(BorrowCommonCustomize borrowCommonCustomize);

	/**
	 * 标的相应的
	 * @param borrowNid
	 * @param result
	 * @param loginUserId 
	 * @return
	 */
	public JSONObject debtRegistSearch(String borrowNid, JSONObject result, int loginUserId);
}
