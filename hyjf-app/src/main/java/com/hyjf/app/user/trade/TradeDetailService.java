/**
 * Description：用户交易明细service接口
 * Copyright: Copyright (HYJF Corporation) 2015
 * Company: HYJF Corporation
 * @author: 王坤
 * @version: 1.0
 * Created at: 2015年11月11日 下午2:17:31
 * Modification History:
 * Modified by :
 */
package com.hyjf.app.user.trade;

import java.util.List;
import java.util.Map;

import com.hyjf.app.BaseService;
import com.hyjf.mybatis.model.customize.app.AppAccountTradeListCustomize;
import com.hyjf.mybatis.model.customize.app.AppTradeListCustomize;

public interface TradeDetailService extends BaseService {

	/**
	 * 查询用户交易明细的交易类型
	 * @return
	 */
	List<AppAccountTradeListCustomize> searchTradeTypes();

	int countTradeDetailListRecordTotal(Map<String, Object> params);

	List<AppTradeListCustomize> searchTradeDetailList(Map<String, Object> params);

}
