package com.hyjf.api.server.user.withdraw;

import com.hyjf.base.bean.BaseDefine;

/**
 * 外部服务接口:用户提现Define
 * 
 * @author liuyang
 *
 */
public class UserWithdrawDefine extends BaseDefine {
	/** 用户提现 @ReqeustMapping */
	public static final String REQUEST_MAPPING = "/server/user/withdraw";
	/** 提现 @RequestMapping */
	public static final String WITHDRAW_ACTION = "/withdraw";
	/** 提现失败页面 路径 */
	public static final String WITHDRAW_ERROR_PATH = "/withdraw/withdraw_cash_fail";
	/** 提现同步回调处理 @ReqestMapping */
	public static final String RETURN_MAPPING = "/return";
	/** 提现异步回调处理 @ReqestMapping */
	public static final String CALLBACK_MAPPING = "/callback";
	/** 类名 */
	public static final String THIS_CLASS = UserWithdrawServer.class.getName();
	/** 提现回调页面 */
	public static final String CALLBACK_VIEW = "/callback/callback_post";
	
	/** 提现 @RequestMapping */
    public static final String GET_USER_WITHDRAW_RECORD_ACTION = "/getUserWithdrawRecord";
}
