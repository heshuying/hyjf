package com.hyjf.api.web.borrow.tender;

public class UserBorrowTenderDefine {

	/**
	 * 用户注册请求类路径
	 */
	protected static final String BORROW_TENDER_REQUEST_MAPPING_CLASS = "/userBorrowTenderServer";
	/**
	 * 用户注册请求方法路径
	 */
	protected static final String BORROW_TENDER_REQUEST_ACTION = "/userBorrowTender";
	
	/** 弹溜溜测试秘钥 */
	protected static final String TEST_TANLIULIU_ACCESSKEY = "test.tanliuliu.accesskey";
	
	/** 弹溜溜正式秘钥 */
	protected static final String RELEASE_TANLIULIU_ACCESSKEY = "release.tanliuliu.accesskey";
	
	/** 标的url前缀  */
	protected static final String BORROW_PREFIX_URL = "borrow.prefix.url";
	
	/** 渠道来源 */
	protected static final String FROM = "from";
	/** 返回状态 */
	protected static final String STATUS = "status";
	/** 汇盈平台用户名 */
	protected static final String USER_NAMEP = "usernamep";
	/** 第三方平台用户名 */
	protected static final String USER_NAME = "username";
	/** 请求时间戳 */
	protected static final String TIMESTAMP = "timestamp";
	/** 开始时间 */
	protected static final String START_TIME = "starttime";
	/** 结束时间 */
	protected static final String END_TIME = "endtime";
	/** 错误消息 */
	protected static final String ERROR_MESSAGE = "errmsg";
	/** 验签 */
	protected static final String SIGN = "sign";

	/** 失败标记 */
	protected static final String FAILED_CODE = "1";
	/** 成功标记 */
	protected static final String SUCCESS_CODE = "0";
	/** 分隔符 */
	protected static final String SPRED_CODE = "/";
	/** 渠道错误 */
	protected static final String ERRORS_FROM = "errors.from"; 
	/** 系统异常的错误消息 */
	protected static final String ERRORS_EXCEPTION_INFO = "errors.exception.info";
	/** 用户名必须 */
	protected static final String ERRORS_USERNAME_REQUIRED = "errors.username.required"; 
	/** 用户名必须 */
	protected static final String ERRORS_USERNAMEP_REQUIRED = "errors.username.required"; 
	/** 时间戳必须 */
	protected static final String ERRORS_TIMESTAMP_REQUIRED = "errors.timestamp.required";
	/** 开始时间必须 */
	protected static final String ERRORS_STARTTIME_REQUIRED = "errors.starttime.required";
	/** 结束时间必须 */
	protected static final String ERRORS_ENDTIME_REQUIRED = "errors.endtime.required";
	/** 用户不存在 */
	protected static final String ERRORS_USERNAME_NO_EXISTS = "errors.username.no.exists";
	/** 验签失败 */
	protected static final String ERRORS_SIGN = "errors.sign"; 
}
