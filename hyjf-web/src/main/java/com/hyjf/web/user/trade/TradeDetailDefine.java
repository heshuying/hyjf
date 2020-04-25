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
package com.hyjf.web.user.trade;

import com.hyjf.web.BaseDefine;

public class TradeDetailDefine extends BaseDefine {

	/** 用户交易明细 @RequestMapping值 */
	public static final String REQUEST_MAPPING = "/user/trade";
	
    /** 初始化指定类型的项目列表 @RequestMapping值 */
    public static final String INIT_TRADE_LIST_ACTION = "/initTradeList";

	/** 收支明细 @RequestMapping值 */
	public static final String TRADE_LIST_ACTION = "tradelist";

	/** 用户充值记录 @RequestMapping值 */
	public static final String RECHARGE_LIST_ACTION = "rechargelist";

	/** 用户提现记录 @RequestMapping值 */
	public static final String WITHDRAW_LIST_ACTION = "withdrawlist";

	/** 交易类型@RequestMapping值 */
	public static final String TRADE_TYPES_ACTION = "tradetypes";
	
	/** 交易明细页面地址 @Path值 */
    public static final String TRADE_LIST_PTAH = "user/trade/tradeList";
    

	/** 类名 */
	public static final String THIS_CLASS = TradeDetailController.class.getName();

}
