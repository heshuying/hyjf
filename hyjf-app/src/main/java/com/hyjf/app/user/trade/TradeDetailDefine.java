/**
 * Description：用户交易明细常量定义
 * Copyright: Copyright (HYJF Corporation) 2015
 * Company: HYJF Corporation
 * @author: 王坤
 * @version: 1.0
 * Created at: 2015年11月11日 下午2:17:31
 * Modification History:
 * Modified by :
 */
package com.hyjf.app.user.trade;

import com.hyjf.app.BaseDefine;

public class TradeDetailDefine extends BaseDefine {

	/** 用户交易明细 @RequestMapping值 */
	public static final String REQUEST_MAPPING = "/user/trade";

	/** 收支明细 @RequestMapping值 */
	public static final String TRADE_LIST_ACTION = "/getTradeList";

	/** 交易类型@RequestMapping值 */
	public static final String TRADE_TYPES_ACTION = "/getTradeTypes";

	/** 类名 */
	public static final String THIS_CLASS = TradeDetailController.class.getName();

}
