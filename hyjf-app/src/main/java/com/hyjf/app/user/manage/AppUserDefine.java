package com.hyjf.app.user.manage;

import com.hyjf.app.BaseDefine;
import com.hyjf.common.util.PropUtils;

public class AppUserDefine extends BaseDefine {
	/** 发布地址 */
	private static final String HOST = PropUtils.getSystem("hyjf.web.host");

	/** 统计类名 */
	public static final String THIS_CLASS = AppUserController.class.getName();

	/** REQUEST_MAPPING */
	public static final String REQUEST_MAPPING = "/appUser";

	/** 登录 */
	public static final String LOGIN_IN_ACTION = "/loginInAction";

	/** 二维码URL */
	public static final String GET_QRCODE_ACTION = "/getQrCodeAction";

	/** 获取用户相关数据 */
	public static final String GET_USERINFO_ACTION = "/getUserinfoAction";

	/** 上传头像 */
	public static final String UPLOAD_AVATAR_ACTION = "/uploadAvatarAction";

	/** 获取紧急联系人类型 */
	public static final String GET_RELATIONTYPE_ACTION = "/getRelationTypes";

	/** 修改联系人 */
	public static final String UPDATE_URGENT_ACTION = "/updateUrgentAction";

	/** 修改昵称 */
	public static final String UPDATE_NICKNAME_ACTION = "/updateNicknameAction";

	/** 注册 */
	public static final String REGIST_ACTION = "/registAction";

	/** 修改密码 */
	public static final String UPDATEPASSWORD_ACTION = "/updatePasswordAction";

	/** 发送验证码 */
	public static final String SEND_VERIFICATIONCODE_ACTION = "/sendVerificationCodeAction";
	/** 验证验证码 */
	public static final String VALIDATE_VERIFICATIONCODE_ACTION = "/validateVerificationCodeAction";
	/** 绑定新手机 */
	public static final String BIND_NEWPHONE_ACTION = "/bindNewPhoneAction";
	/** 用户退出登录 */
	public static final String LOGIN_OUT_ACTION = "/loginOutAction";
	/** 找回密码 */
	public static final String GETBACK_PASSWORD_ACTION = "/getBackPasswordAction";

	// ------------------------------------Request--------------------------------------------
	/** 登录Request */
	public static final String LOGIN_IN_REQUEST = REQUEST_HOME + REQUEST_MAPPING + LOGIN_IN_ACTION;
	/** 获取用户相关数据 */
	public static final String GET_USERINFO_REQUEST = REQUEST_HOME + REQUEST_MAPPING + GET_USERINFO_ACTION;
	/** 上传头像 */
	public static final String UPLOAD_AVATAR_REQUEST = REQUEST_HOME + REQUEST_MAPPING + UPLOAD_AVATAR_ACTION;
	/** 获取紧急联系人类型 */
	public static final String GET_RELATIONTYPE_REQUEST = REQUEST_HOME + REQUEST_MAPPING + GET_RELATIONTYPE_ACTION;
	/** 修改联系人 */
	public static final String UPDATE_URGENT_REQUEST = REQUEST_HOME + REQUEST_MAPPING + UPDATE_URGENT_ACTION;
	/** 修改昵称 */
	public static final String UPDATE_NICKNAME_REQUEST = REQUEST_HOME + REQUEST_MAPPING + UPDATE_NICKNAME_ACTION;
	/** 注册 */
	public static final String REGIST_REQUEST = REQUEST_HOME + REQUEST_MAPPING + REGIST_ACTION;
	/** 修改密码 */
	public static final String UPDATEPASSWORD_REQUEST = REQUEST_HOME + REQUEST_MAPPING + UPDATEPASSWORD_ACTION;
	/** 发送验证码 */
	public static final String SEND_VERIFICATIONCODE_REQUEST = REQUEST_HOME + REQUEST_MAPPING + SEND_VERIFICATIONCODE_ACTION;
	/** 验证验证码 */
	public static final String VALIDATE_VERIFICATIONCODE_REQUEST = REQUEST_HOME + REQUEST_MAPPING + VALIDATE_VERIFICATIONCODE_ACTION;
	/** 绑定新手机 */
	public static final String BIND_NEWPHONE_REQUEST = REQUEST_HOME + REQUEST_MAPPING + BIND_NEWPHONE_ACTION;
	/** 用户退出登录 */
	public static final String LOGIN_OUT_REQUEST = REQUEST_HOME + REQUEST_MAPPING + LOGIN_OUT_ACTION;
	/** 找回密码 */
	public static final String GETBACK_PASSWORD_REQUEST = REQUEST_HOME + REQUEST_MAPPING + GETBACK_PASSWORD_ACTION;
	// ====================================================================================
	// 处理结果
	/** 处理成功 */
	public static final String SUCCESS = "0";
	/** 处理失败 */
	public static final String FAILED = "1";

	// 验证码类型
	/** 注册 */
	public static final String PARAM_TPL_ZHUCE = "TPL_ZHUCE";
	/** 找回密码 */
	public static final String PARAM_TPL_ZHAOHUIMIMA = "TPL_ZHAOHUIMIMA";
	/** 更换手机号-验证原手机号 */
	public static final String PARAM_TPL_YZYSJH = "TPL_YZYSJH";
	/** 更换手机号-绑定新手机号 */
	public static final String PARAM_TPL_BDYSJH = "TPL_BDYSJH";
	/** 提现放欺诈-验证码校验 */
	public static final String PARAM_TPL_SMS_WITHDRAW = "TPL_SMS_WITHDRAW";

	/** 短信验证码状态,新验证码 */
	public static final Integer CKCODE_NEW = 0;
	/** 短信验证码状态,失效 */
	public static final Integer CKCODE_FAILED = 7;
	/** 短信验证码状态,已验 */
	public static final Integer CKCODE_YIYAN = 8;
	/** 短信验证码状态,已用 */
	public static final Integer CKCODE_USED = 9;
	// ================================================
	/** 二维码URL */
	public static final String QRCODE_URL = "/qrcode";

	// 江西银行绑卡状态
	/** 已绑卡 */
	public static final Integer BANK_BINDCARD_STATUS_SUCCESS = 1;
	/** 未绑卡 */
	public static final Integer BANK_BINDCARD_STATUS_FAIL = 0;

	/** 获取注册结果 */
	public static final String REGIST_RESULT_ACTION = "/user/regist/result/{state}";

	/** 注册成功跳转页面 */
	public static final String REGIST_RESULT_SUCCESS = "/user/regist/result/success";

	/** 状态描述 */
	public static final String STATUS_FAIL_MSG = "请求参数非法";

	/** 修改手机号失败页面 */
	public static final String SET_MOBILE_FAIL = "/user/setting/mobile/result/failed";
	/** 修改手机号成功页面 */
	public static final String SET_MOBILE_SUCCESS = "/user/setting/mobile/result/success";

	/** 我的奖励Url */
	public static final String REWARD_URL = "/user/reward";
}
