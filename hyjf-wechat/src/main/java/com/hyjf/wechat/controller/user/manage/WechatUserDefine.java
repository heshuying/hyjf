package com.hyjf.wechat.controller.user.manage;

import com.hyjf.common.util.PropUtils;
import com.hyjf.wechat.base.BaseDefine;

public class WechatUserDefine extends BaseDefine {

	/** 发布地址 */
	private static final String HOST = PropUtils.getSystem("hyjf.web.host");

	/** 统计类名 */
	public static final String THIS_CLASS = WechatUserController.class.getName();

	/** REQUEST_MAPPING */
	public static final String REQUEST_MAPPING = "/wechatUser";

	/** 验证验证码 */
	public static final String VALIDATE_VERIFICATIONCODE_ACTION = "/validateVerificationCodeAction";

	// 验证码类型
	/** 注册 */
	public static final String PARAM_TPL_ZHUCE = "TPL_ZHUCE";
	/** 找回密码 */
	public static final String PARAM_TPL_ZHAOHUIMIMA = "TPL_ZHAOHUIMIMA";
	/** 更换手机号-验证原手机号 */
	public static final String PARAM_TPL_YZYSJH = "TPL_YZYSJH";
	/** 更换手机号-绑定新手机号 */
	public static final String PARAM_TPL_BDYSJH = "TPL_BDYSJH";
	/** 短信验证码状态,新验证码 */
	public static final Integer CKCODE_NEW = 0;
	/** 短信验证码状态,已验 */
	public static final Integer CKCODE_YIYAN = 8;

	/** 验证验证码 */
	public static final String VALIDATE_VERIFICATIONCODE_REQUEST = REQUEST_HOME + REQUEST_MAPPING
			+ VALIDATE_VERIFICATIONCODE_ACTION;
}
