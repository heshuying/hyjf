/**
 * Description:（类功能描述-必填） 需要在每个方法前添加业务描述，方便业务业务行为的BI工作
 * Copyright: Copyright (HYJF Corporation)2018
 * Company: HYJF Corporation
 * @author: PC-LIUSHOUYI
 * @version: 1.0
 * Created at: 2018年1月31日 下午2:31:37
 * Modification History:
 * Modified by : 
 */
	
package com.hyjf.mybatis.mapper.customize;

import java.util.List;

import com.hyjf.mybatis.model.customize.ActdecListedFourCustomize;

/**
 * @author PC-LIUSHOUYI
 */

public interface ActdecListedFourCustomizeMapper {
	
	Integer countActdecListedFour(ActdecListedFourCustomize actdecListedFourCustomize);
	List<ActdecListedFourCustomize> selectActdecListedFourList(ActdecListedFourCustomize actdecListedFourCustomize);
	List<ActdecListedFourCustomize> exportActdecListedFourList(ActdecListedFourCustomize actdecListedFourCustomize);
}

	