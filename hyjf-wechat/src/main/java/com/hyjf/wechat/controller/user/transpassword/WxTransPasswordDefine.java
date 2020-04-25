/**
 * 设置交易密码
 */
package com.hyjf.wechat.controller.user.transpassword;

import com.hyjf.wechat.base.BaseDefine;

public class WxTransPasswordDefine extends BaseDefine {

	/** @RequestMapping值 */
	public static final String REQUEST_MAPPING = "/wx/transpassword";
	
	
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
	
	//APP改版 start
	/** 设置交易密码验证失败*/
	public static final String JUMP_HTML_FAILED_PATH = "/user/setting/bankPassword/result/failed";
	/** 设置交易密码处理中*/
	public static final String JUMP_HTML_HANDLING_PATH = "/user/setting/bankPassword/result/handling";
	/** 设置交易密码成功*/
	public static final String JUMP_HTML_SUCCESS_PATH = "/user/setting/bankPassword/result/success";

	//APP改版 end
	
	
	
	/** 设置密码成功页面*/
    public static final String SET_PASSWORD_PATH = "/bank/user/personalsetting/setpsw";
	/** 设置密码成功页面*/
	public static final String PASSWORD_SUCCESS_PATH = "/user/setting/bankPassword/result/success";
	/** 设置密码失败页面*/
	public static final String PASSWORD_ERROR_PATH = "/bank/user/personalsetting/error";
	/** 设置密码中间页面*/
	public static final String PASSWORD_MIDDLE_PATH = "/bank/user/personalsetting/initMobile";


	/** 发送短信验证码 @RequestMapping值 */
    public static final String TRANS_PASSWORD_SEND_CODE_ACTION = "transPasswordSendCode";
    /** 校验验证码 @RequestMapping值 */
    public static final String TRANS_PASSWORD_CHECK_CODE_ACTION = "transPasswordCheckCode";
    
    
    /** 成功结果@RequestMapping值 */
    public static final Boolean STATUS_TRUE = true;
    /** 失败结果@RequestMapping值 */
    public static final Boolean STATUS_FALSE = false;
	/** 当前controller名称 */
	public static final String THIS_CLASS = WxTransPasswordController.class.getName();
	

    /**本地跳转的页面*/
    public static final String JUMP_HTML = "/jumpHTML";
    /**前端的url*/
    public static final String JUMP_HTML_ERROR_PATH = "/user/setting/bankPassword/result/failed";
    
}
