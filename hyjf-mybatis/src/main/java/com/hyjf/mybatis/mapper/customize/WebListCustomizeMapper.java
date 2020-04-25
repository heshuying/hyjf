package com.hyjf.mybatis.mapper.customize;

import java.util.List;

import com.hyjf.mybatis.model.customize.WebListCustomize;

public interface WebListCustomizeMapper {

	/**
	 * 取得列表
	 * @param webListCustomize
	 * @return
	 */
	public List<WebListCustomize> selectBorrowRecoverOfWeb(WebListCustomize webCustomize) ;


}

