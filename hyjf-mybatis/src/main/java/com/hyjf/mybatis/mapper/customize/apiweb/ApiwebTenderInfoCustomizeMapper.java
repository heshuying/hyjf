/**
 * 出借记录查询
 */

package com.hyjf.mybatis.mapper.customize.apiweb;

import java.util.List;
import java.util.Map;

import com.hyjf.mybatis.model.customize.apiweb.ApiwebTenderInfoCustomize;


public interface ApiwebTenderInfoCustomizeMapper {

	/**
	 * 根据用户编号 出借记录查询
	 * @return
	 */
	List<ApiwebTenderInfoCustomize> getTenderInfoList(Map<String,Object> paramMap);

}
