/**
 * 出借信息
 */

package com.hyjf.mybatis.mapper.customize.apiweb;

import java.util.Map;

import com.hyjf.mybatis.model.customize.apiweb.BorrowTenderInfoCustomize;


public interface GetCardCustomizeMapper {
	
	/**
	 * 出借是否符合条件
	 * @param paramMap
	 * @return
	 */
	BorrowTenderInfoCustomize getBorrowTender(Map<String,Object> paramMap);

}
