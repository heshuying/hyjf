/**
 * 设置交易密码
 */
package com.hyjf.web.bank.web.user.transpassword;

import com.hyjf.web.BaseDefine;

public class TransPasswordDefine extends BaseDefine {

	/** @RequestMapping值 */
	public static final String REQUEST_MAPPING = "/bank/user/transpassword";
	
	/** 跳转到账户总览页面 */
	public static final String REDIRECT_PANDECT = "redirect:/user/pandect/pandect.do";
	
	/** 跳转到安全中心页面 */
	public static final String REDIRECT_SAFE = "redirect:/user/safe/init.do";
	
	/** @RequestMapping值 */
	public static final String INIT = "/init";
	/** @RequestMapping值 */
	public static final String SETPASSWORD_ACTION = "/setPassword";
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
	public static final String INIT_MOBILE = "/initMobile";
	/** 修改手机号 */
	public static final String MOBILEMODIFY_ACTION = "/mobileModify";
	/** 修改手机号(平台手机号) */
	public static final String PLAT_MOBILE_MODIFY_ACTION = "/platMobileModify";
	/** 修改手机号成功请求 */
	public static final String MOBILEMODIFY_SUCCESS_ACTION = "/successMobile";
	
	
	/** 设置密码成功页面*/
	public static final String PASSWORD_SUCCESS_PATH = "/bank/user/personalsetting/success";
	/** 设置密码失败页面*/
	public static final String PASSWORD_ERROR_PATH = "/bank/user/personalsetting/error";
	/** 设置密码中间页面*/
	public static final String PASSWORD_MIDDLE_PATH = "/bank/user/personalsetting/initMobile";

	
	/** 原：修改平台手机号初始化页面   现：为跳开户画面*/    
	public static final String INIT_PLAT_MOBILE_PATH = "forward:/bank/web/user/bankopen/init.do";/*原：/user/personalsetting/platMobile*/
	
	/** 修改手机号初始化页面 */
	public static final String INIT_MOBILE_PATH = "/bank/user/personalsetting/initMobile";
	/** 修改手机号成功页面*/
	public static final String MOBILE_SUCCESS_PATH = "/bank/user/personalsetting/mobileSuccess";

	
	/** 发送短信验证码 @RequestMapping值 */
	public static final String SEND_CODE_ACTION = "sendCode";
	/** 校验验证码 @RequestMapping值 */
	public static final String CHECK_CODE_ACTION = "checkCode";
	
	/** 江西银行 发送短信码 */
	public static final String SEND_PLUS_CODE_ACTION = "sendPlusCode";
	
	
	/** 当前controller名称 */
	public static final String THIS_CLASS = TransPasswordController.class.getName();
	
	/** 验证手机号 */
	public static final String CHECK_PHONE = "checkPhone";
	
}
