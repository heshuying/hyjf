package com.hyjf.api.web.regist;

public class UserRegistDefine {

	/**
	 * 用户注册请求类路径
	 */
	protected static final String REGIST_REQUEST_MAPPING_CLASS = "/userRegistServer";
	/**
	 * 用户注册请求方法路径
	 */
	protected static final String REGIST_REQUEST_ACTION = "/userRegist";
	
	/** 渠道来源 */
	protected static final String FROM = "from";
	/** 请求时间戳 */
	protected static final String TIMESTAMP = "timestamp";

	/** 注册时间戳 */
	protected static final String REG_TIME = "regtime";
	/** 验签 */
	protected static final String SIGN = "sign";
	
	/** 用户名不能为空 */
	protected static final String USER_NAME = "username";
	
	/** 汇盈平台用户名 */
	protected static final String USER_NAMEP = "usernamep";
	

	/** 手机号码不能为空 */
	protected static final String MOBILE = "mobile";

	/** 邮箱不是一个有效的电子邮件地址 */
	protected static final String USER_EMAIL = "email";
	
	/** 返回状态 */
	protected static final String STATUS = "status";
	
	/** 错误消息 */
	protected static final String ERROR_MESSAGE = "errmsg";
	
	/** 渠道错误 */
	protected static final String ERRORS_FROM = "errors.from"; 
	/** 用户名必须 */
	protected static final String ERRORS_USERNAME_REQUIRED = "errors.username.required"; 
	/** 该用户已存在*/
	protected static final String ERRORS_USERNAME_EXISTS = "errors.username.exists";
	/** 用户名格式错误 */
	protected static final String ERRORS_USERNAME_OTHER = "errors.username.other"; 
	/** 手机号必须 */
	protected static final String ERRORS_MOBILE_REQUIRED = "errors.mobile.required"; 
	/** 手机号格式不正确 */
	protected static final String ERRORS_MOBILE_OTHER = "errors.mobile.other";
	/** 手机号已经存在 */
	protected static final String ERRORS_MOBILE_EXISTS = "errors.mobile.exists";
	/** 电子邮箱不正确 */
	protected static final String ERRORS_EMAIL_OTHER = "errors.email.other"; 
	/** 电子邮箱不正确 */
	protected static final String ERRORS_EMAIL_EXISTS = "errors.email.exists"; 
	/** 时间戳必须 */
	protected static final String ERRORS_TIMESTAMP_REQUIRED = "errors.timestamp.required"; 
	/** 验签失败 */
	protected static final String ERRORS_SIGN = "errors.sign"; 
	/** 失败标记 */
	protected static final String FAILED_CODE = "1";
	/** 成功标记 */
	protected static final String SUCCESS_CODE = "0";
	/** 系统异常的错误消息 */
	protected static final String ERRORS_EXCEPTION_INFO = "errors.exception.info";
	/**  */
	protected static final String TEST_TANLIULIU_ACCESSKEY = "test.tanliuliu.accesskey";
	
	/**  */
	protected static final String RELEASE_TANLIULIU_ACCESSKEY = "release.tanliuliu.accesskey";
	/** 短信模板名称 */
	protected static final String TPL_SMS_AUTO_REGIST_NAME = "TPL_AUTOREGIST";
	
	
}
