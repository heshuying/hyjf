/**
 * Description:（类功能描述-必填） 需要在每个方法前添加业务描述，方便业务业务行为的BI工作
 * Copyright: Copyright (HYJF Corporation)2017
 * Company: HYJF Corporation
 * @author: LIBIN
 * @version: 1.0
 * Created at: 2017年10月12日 下午2:11:50
 * Modification History:
 * Modified by : 
 */
package com.hyjf.api.aems.tradelist;

import com.hyjf.api.server.tradelist.TradeListBean;
import com.hyjf.base.service.BaseService;
import com.hyjf.mybatis.model.customize.AccountDetailCustomize;

import java.util.List;

/**
 * @author LIBIN
 */
public interface AemsTradeListService extends BaseService {
	
	/**
	 * 查询用户收支明细列表
	 * @param trade
	 * @param limitStart
	 * @param limitEnd
	 * @return
	 */
	List<AccountDetailCustomize> searchTradeList(AemsTradeListBean trade);
	
	/**
	 * 查询手机号是否存在
	 * @param trade
	 * @param limitStart
	 * @param limitEnd
	 * @return
	 */
	boolean existPhone(String mobile);
	
	/**
	 * 查询手机号與accountId是否一致
	 * @param trade
	 * @param limitStart
	 * @param limitEnd
	 * @return
	 */
	boolean existAccountId(String mobile, String accountId);
	
}
