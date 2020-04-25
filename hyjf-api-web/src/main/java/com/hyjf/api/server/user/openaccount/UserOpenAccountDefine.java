package com.hyjf.api.server.user.openaccount;

import com.hyjf.base.bean.BaseDefine;

/**
 * 外部服务接口:用户开户
 * 
 * @author liuyang
 *
 */
public class UserOpenAccountDefine extends BaseDefine {
	/** 外部服务接口:用户注册 @RequestMapping */
	public static final String REQUEST_MAPPING = "/server/user/openaccount";
	/** 开户发送短信验证码 @ReqeustMapping */
	public static final String SEND_SMS_ACTION = "/sendCode";
	/** 用户开户 @RequestMapping */
	public static final String OPEN_ACCOUNT_ACTION = "/open";
	/** 静默开户 @RequestMapping */
	public static final String OPEN_ACCOUNT_SILENT_ACTION = "/openSilent";
	/** 类名 */
	public static final String THIS_CLASS = UserOpenAccountServer.class.getName();

}
