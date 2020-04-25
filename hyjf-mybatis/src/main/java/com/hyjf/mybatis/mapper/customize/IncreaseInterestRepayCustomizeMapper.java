/**
 * Description:（类功能描述-必填） 需要在每个方法前添加业务描述，方便业务业务行为的BI工作
 * Copyright: Copyright (HYJF Corporation)2018
 * Company: HYJF Corporation
 * @author: PC-LIUSHOUYI
 * @version: 1.0
 * Created at: 2018年1月8日 下午5:22:51
 * Modification History:
 * Modified by : 
 */

/**
 * @author PC-LIUSHOUYI
 */

package com.hyjf.mybatis.mapper.customize;

import com.hyjf.mybatis.model.auto.IncreaseInterestRepayExample;

/**
 * @author PC-LIUSHOUYI
 */

public interface IncreaseInterestRepayCustomizeMapper {
	
	/**
	 * 取得合计金额
	 * 
	 * @param param
	 * @return
	 */
	public String sumAccount(IncreaseInterestRepayExample example);
}

	