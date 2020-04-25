/**
 * Description:（类功能描述-必填） 需要在每个方法前添加业务描述，方便业务业务行为的BI工作
 * Copyright: Copyright (HYJF Corporation)2015
 * Company: HYJF Corporation
 * @author: Administrator
 * @version: 1.0
 * Created at: 2015年11月18日 下午12:34:08
 * Modification History:
 * Modified by : 
 */

package com.hyjf.web.user.login;

import com.hyjf.web.BaseDefine;

/**
 * @author Administrator
 */

public class LoginDefine extends BaseDefine {

    /** CONTROLLOR @value值 */
    public static final String CONTROLLOR_CLASS_NAME = "LoginController";

    /** CONTROLLOR @RequestMapping值 */
    public static final String CONTROLLOR_REQUEST_MAPPING = "/user/login";

    /** 路径 */
    public static final String INIT_PATH = "user/login/login";

    /** 列表画面 @RequestMapping值 */
    public static final String INIT = "init";

    /** 登录 @RequestMapping值 */
    public static final String LOGIN = "login";
	
	/** 检查用户名 @RequestMapping值 */
    public static final String CHECKUSER = "checkUserAction";
    /** 登出 @RequestMapping值 */
    public static final String LOGINOUT = "logout";
	/** 第三方登录 @RequestMapping值 */
    public static final String LOGIN_THREEPART = "threepartLogin";
    

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
	
	/** 账户手机号不一致错误 */
    public static final String USER_NAME_MOBILE_ERROR = "userNameAndMobile";

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
	
}
