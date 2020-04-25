package com.hyjf.api.server.user.accountopenpage;

import com.hyjf.base.bean.BaseDefine;

public class OpenAccountPageDefine extends BaseDefine {
	/** 外部服务接口:用户注册 @RequestMapping */
	public static final String REQUEST_MAPPING = "/server/user/openAccountPage";
	/** 开户发送短信验证码 @ReqeustMapping */
	public static final String SEND_SMS_ACTION = "/sendCode";
	/** 用户开户 @RequestMapping */
	public static final String OPEN_ACCOUNT_ACTION = "/open";
	/** 同步回调  */
    public static final String RETURL_SYN_ACTION = "/openaccountReturn";
    /** 异步回调  */
    public static final String RETURL_ASY_ACTION = "/openaccountBgreturn";
	
	/** 静默开户 @RequestMapping */
	public static final String OPEN_ACCOUNT_SILENT_ACTION = "/openSilent";
	/** 类名 */
	public static final String THIS_CLASS = OpenAccountPageServer.class.getName();
	
	/**错误页面*/
    public static final String PATH_OPEN_ACCOUNT_PAGE_ERROR = "/bank/user/trusteePay/error";

}
