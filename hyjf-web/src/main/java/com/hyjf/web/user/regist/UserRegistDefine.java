package com.hyjf.web.user.regist;

import com.hyjf.web.BaseDefine;

public class UserRegistDefine extends BaseDefine {
    /** CONTROLLOR @value值 */
    public static final String CONTROLLOR_CLASS_NAME = "UserRegistDefine";

	/** 权限 CONTROLLOR @RequestMapping值 */
	public static final String REQUEST_MAPPING = "/user/regist";

	/** 生成验证码 @RequestMapping值 */
	public static final String REGIST_RANDOMCODE = "getcaptcha";

	/** 检查验证码 @RequestMapping值 */
	public static final String REGIST_CHECK_RANDOMCODE = "checkcaptcha";

	/** 检查验证码 @RequestMapping值 */
	public static final String REGIST_CHECK_RANDOMCODE_JSON = "checkcaptchajson";
	
	/** 初始化用户注册画面 @RequestMapping值 */
	public static final String INIT_REGIST_ACTION = "initregist";

	/** 初始化开户画面 @RequestMapping值 */
	public static final String REGIST_CHECK_ACTION = "checkaction";

	/** 用户注册 @RequestMapping值 */
	public static final String USER_REGIST_ACTION = "regist";
	/** 跳转到注册成功页面 */
	public static final String TO_REGIST_SUCCESS_ACTION = "to_regist_success";
	/** 跳转到注册成功优惠券页面 */
    public static final String TO_REGIST_SUCCESS_COUPON_ACTION = "to_regist_success_coupon";

    /** 企业用户注册成功画面 */
    public static final String TO_BUSINESS_USERS_REGISTER_SUCCESS = "businessUsersRegisterSuccess";

    /** 企业用户注册成功 页面 */
	public static final String BUSINESS_USERS_REGISTER_SUCCESS_PAGE = "user/regist/regist_success_coupon_Borrower";

	/** 企业用户开户指南画面 */
	public static final String TO_BUSINESS_USERS_GUIDE = "businessUsersGuide";

	/** 企业用户开户指南 页面 */
	public static final String BUSINESS_USERS_GUIDE_PAGE = "user/regist/enterprise-open-guide";

	/** 初始化短信验证页面 @RequestMapping值 */
	public static final String INIT_MOBILE_ACTION = "initmobile";

	/** 发送短信验证码 @RequestMapping值 */
	public static final String RETURN_REGIST_ACTION = "returnregist";

	/** 发送短信验证码 @RequestMapping值 */
	public static final String REGIST_SEND_CODE_ACTION = "sendcode";

	/** 校验验证码 @RequestMapping值 */
	public static final String REGIST_CHECK_CODE_ACTION = "checkcode";
	
    /** 路径 */
    public static final String INIT_PATH = "user/regist/regist";

    /** 列表画面 @RequestMapping值 */
    public static final String INIT = "init";

	/** 校验用户名是否存在 @RequestMapping值 */
	public static final String REGIST_CHECK_USER_NAME_ACTION = "checkUserName";
	
	/** 校验手机号是否存在 @RequestMapping值 */
	public static final String REGIST_CHECK_MOBILE_ACTION = "checkMobile";
	
	/** 校验推荐人是否存在 @RequestMapping值 */
	public static final String REGIST_CHECK_RECOMMEND_ACTION = "checkRecommend";

	/** 初始化短信验证成功画面 @RequestMapping值 */
	public static final String REGIST_SUCCESS_ACTION = "registsuccess";

	/** 初始化开户画面 @RequestMapping值 */
	public static final String REGIST_ACCOUNT_ACTION = "account";

	/** 判断是否登录 @RequestMapping值 */
	public static final String REGIST_ISLOGIN = "islogin";

	/** 初始化用户注册画面 @RequestMapping值 */
	public static final String INIT_REGIST_USER_PATH = "user/regist/initRegist";

	/** 初始化短信验证页面 @RequestMapping值 */
	public static final String INIT_REGIST_MOBILE_PATH = "user/regist/mobile";

	/** 初始化短信验证成功画面 @RequestMapping值 */
    public static final String REGIST_SUCCESS_PATH = "user/regist/regist_success";
	/** 初始化短信验证成功画面 @RequestMapping值 */
	public static final String REGIST_SUCCESS_COUPON_PATH = "user/regist/regist_success_coupon";

	/** 初始化开户画面 @RequestMapping值 */
	public static final String REGIST_ACCOUNT_PATH = "user/account/openAccount";

	/** 当前controller名称 */
	public static final String THIS_CLASS = UserRegistController.class.getName();

	/** 用户注册表单 */
	public static final String REGTIST_USER_FORM = "userregistForm";

	/** 用户名 */
	public static final String USER_NAME = "用户名";

	/** 用户名不能为空 */
	public static final String USER_NAME_ERROR = "userName";

	/** 用户邮箱 */
	public static final String USER_EMAIL = "用户邮箱";

	/** 邮箱不是一个有效的电子邮件地址 */
	public static final String USER_EMAIL_ERROR = "email";

	/** 用户密码 */
	public static final String PASSWORD = "用户密码";

	/** 用户密码不能为空 */
	public static final String PASSWORD_ERROR = "password";

	/** 确认密码 */
	public static final String RE_PASSWORD = "确认密码";

	/** 确认密码不能为空 */
	public static final String RE_PASSWORD_ERROR = "passwordConfirm";

	/** 用户手机号码 */
	public static final String MOBILE = "手机号码";

	/** 手机号码不能为空 */
	public static final String MOBILE_ERROR = "mobile";

	/** 短信验证码 */
	public static final String CODE = "短信验证码";

	/** 短信验证码不能为空 */
	public static final String CODE_ERROR = "code";

	/** 注册平台 */
	public static final String REGISTPLAT = "注册平台";

	/** 注册平台不能为空 */
	public static final String REGISTPLAT_ERROR = "registPlat";

	/** 推荐人 */
	public static final String RECOMMEND = "推荐人";

	/** 推荐人 不存在 */
	public static final String RECOMMEND_ERROR = "recommend";

	/** 图片验证码 */
	public static final String CAPTCHA = "验证码";

	/** 图片验证码 不正确 */
	public static final String CAPTCHA_ERROR = "captcha";

	/** 第三方注册平台 */
	public static final String UTMID = "验证码";

	/** 第三方注册平台 不正确 */
	public static final String UTMID_ERROR = "utmId";

	/** 必须 */
	public static final String ERROR_TYPE_REQUIRED = "required";
	/** 重复 */
	public static final String ERROR_TYPE_REPEAT = "repeat";
	/** 其他 */
	public static final String ERROR_TYPE_OTHERS = "other";
	/** 其他 */
	public static final String ERROR_MAXCOUNT_OTHERS = "maxCount";
	
	public static final String ERROR_INTERVAL_TIME_OTHERS = "intervaltime";
	
	public static final String ERROR_NOT_EXIST = "notexist";
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
    /** 跳转注册协议 @RequestMapping值 */
    public static final String REGIST_DETAIL = "/goDetail";
    
	/** 初始化开户画面 @RequestMapping值 */
	public static final String REGIST_CHECK_PHONE = "checkPhone";
}
