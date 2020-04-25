package com.hyjf.mybatis.mapper.customize.web;

import com.hyjf.mybatis.model.customize.web.WebBorrowAuthCustomize;

import java.util.List;
import java.util.Map;

public interface WebBorrowAuthCustomizeMapper {
	/**
	 * 查询待授权标的数量
	 * @param params
	 * @return
	 */
	int countBorrowNeedAuthRecordTotal(Map<String, Object> params);
	/**
	 * 查询待授权标的标的列表
	 * @param params
	 * @return
	 */
	List<WebBorrowAuthCustomize> searchBorrowNeedAuthList(Map<String, Object> params);
	
	// 查询标的已授权列表
    int countBorrowAuthedRecordTotal(Map<String, Object> params);
    // 查询标的已授权列表
    List<WebBorrowAuthCustomize> searchBorrowAuthedList(Map<String, Object> params);
}
