/**
 * Description:会员用户开户记录初始化列表查询
 * Copyright: (HYJF Corporation) 2015
 * Company: HYJF Corporation
 * @author: 王坤
 * @version: 1.0
 * Created at: 2015年11月11日 上午11:01:57
 * Modification History:
 * Modified by :
 * */

package com.hyjf.mybatis.mapper.customize.batch;

import java.math.BigDecimal;
import java.util.Map;

public interface BatchDebtDetailCustomizeMapper {

	/**
	 * 查询已承接相应的承接记录本金总和
	 * @param params
	 * @return
	 */
	BigDecimal countDebtDetailCapitalSum(Map<String, Object> params);

	/**
	 * 查询已承接记录的承接记录利息总和
	 * @param params
	 * @return
	 */
	BigDecimal countDebtDetailInterestSum(Map<String, Object> params);


}
