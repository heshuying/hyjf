package com.hyjf.api.server.user.recharge;

import com.hyjf.base.bean.BaseDefine;

/**
 * 外部服务接口:用户充值
 * 
 * @author liuyang
 *
 */
public class UserRechargeDefine extends BaseDefine {

	/** 用户充值 @RequestMapping */
	public static final String REQUEST_MAPPING = "/server/user/recharge";

	/** 充值 @ReqestMapping */
	public static final String RECHARGE_ACTION = "/recharge";

	/** 发送短信验证码 @RequestMapping */
	public static final String SENDCODE_ACTION = "/sendSms";

	/** 类名 */
	public static final String THIS_CLASS = UserRechargeServer.class.getName();

}
