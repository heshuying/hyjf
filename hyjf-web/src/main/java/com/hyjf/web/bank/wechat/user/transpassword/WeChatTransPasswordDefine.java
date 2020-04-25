/**
 * 设置交易密码
 */
package com.hyjf.web.bank.wechat.user.transpassword;

import com.hyjf.web.BaseDefine;

public class WeChatTransPasswordDefine extends BaseDefine {

	/** @RequestMapping值 */
	public static final String REQUEST_MAPPING = "/bank/wechat/user/transpassword";
	
	/** @RequestMapping值 */
	public static final String CHECK = "/check";
	/** @RequestMapping值 */
	public static final String SETPASSWORD_ACTION = "setPassword";
	/** @RequestMapping值 */
	public static final String RETURL_SYN_PASSWORD_ACTION = "/passwordReturn";
	/** @RequestMapping值 */
	public static final String RETURN_ASY_PASSWORD_ACTION = "/passwordBgreturn";
	
	/** 重置交易密码 */
	public static final String RESETPASSWORD_ACTION = "/resetPassword";
	/** 重置交易密码同步回调 */
	public static final String RETURL_SYN_RESETPASSWORD_ACTION = "/resetPasswordReturn";
	/** 重置交易密码异步回调 */
	public static final String RETURN_ASY_RESETPASSWORD_ACTION = "/resetPasswordBgreturn";

	
	/** @RequestMapping值 */
	public static final String CHECK_MOBILE = "/checkMobile";
	/** 修改手机号 */
	public static final String MOBILEMODIFY_ACTION = "/mobileModify";
	/** 修改手机号(平台手机号) */
	public static final String PLAT_MOBILE_MODIFY_ACTION = "/platMobileModify";
	
	/** 设置密码成功页面*/
	public static final String PASSWORD_SUCCESS_PATH = "/user/personalsetting/passwordSuccess";
	/** 设置密码失败页面*/
	public static final String PASSWORD_ERROR_PATH = "/user/personalsetting/passwordError";
	/** 设置密码中间页面*/
	public static final String PASSWORD_MIDDLE_PATH = "/user/personalsetting/passwordMiddle";

	
	/** 修改平台手机号初始化页面 */
	public static final String INIT_PLAT_MOBILE_PATH = "/user/personalsetting/platMobile";
	/** 修改手机号初始化页面 */
	public static final String INIT_MOBILE_PATH = "/user/personalsetting/initMobile";
	/** 修改手机号成功页面*/
	public static final String MOBILE_SUCCESS_PATH = "/user/personalsetting/mobileSuccess";
	/** 修改手机号失败页面*/
	public static final String MOBILE_ERROR_PATH = "/user/personalsetting/mobileError";
	
	/** 发送短信验证码 @RequestMapping值 */
	public static final String SEND_CODE_ACTION = "sendCode";
	/** 校验验证码 @RequestMapping值 */
	public static final String CHECK_CODE_ACTION = "checkCode";
	
	/** 发送短信验证码 @RequestMapping值 */
    public static final String TRANS_PASSWORD_SEND_CODE_ACTION = "transPasswordSendCode";
    /** 校验验证码 @RequestMapping值 */
    public static final String TRANS_PASSWORD_CHECK_CODE_ACTION = "transPasswordCheckCode";
	
	/** 江西银行 发送短信码 */
	public static final String SEND_PLUS_CODE_ACTION = "sendPlusCode";
	
	
	/** 当前controller名称 */
	public static final String THIS_CLASS = WeChatTransPasswordController.class.getName();
    
}
