package com.hyjf.api.server.user.nonwithdraw;

import com.hyjf.base.bean.BaseDefine;

/**
 * 外部服务接口:用户免密提现Define
 * 
 * @author sunss
 *
 */
public class NonCashWithdrawDefine extends BaseDefine {
	/** 用户免密提现 @ReqeustMapping */
	public static final String REQUEST_MAPPING = "/server/user/nonCashWithdraw";
	/** 免密提现 @RequestMapping */
	public static final String NON_CASH_WITHDRAW_ACTION = "/cash";
	/** 免密提现test @RequestMapping */
    public static final String NON_CASH_WITHDRAW_ACTION_TEST = "/cashtest";
	/** 类名 */
	public static final String THIS_CLASS = NonCashWithdrawServer.class.getName();
}
