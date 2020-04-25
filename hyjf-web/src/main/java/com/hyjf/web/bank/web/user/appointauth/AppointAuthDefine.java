package com.hyjf.web.bank.web.user.appointauth;

import com.hyjf.web.BaseDefine;

public class AppointAuthDefine extends BaseDefine  {

	/** 权限 CONTROLLOR @RequestMapping值 */
	public static final String REQUEST_MAPPING = "/bank/user/appointment";

	/** 账户总览    */
	public static final String PANDECT_ACTION = "/pandect";
	
	/** 账户总览画面 路径 */
	public static final String PANDECT_PATH = "user/pandect/pandect";
	
	/** 预约授权    */
	public static final String APPOINTMENT_ACTION = "/appointment";
	
	/** 取消预约授权    */
	public static final String APPOINTMENT_CANCEL_ACTION = "/appointmentCancel";
	
	/** 预约授权成功 路径 */
	public static final String APPOINTMENT_SUCCESS_PATH = "user/pandect/appointment_success";
	
	/** 预约授权失败 路径 */
	public static final String APPOINTMENT_FAIL_PATH = "user/pandect/appointment_fail";
	
	/** 预约授权后同步回调 @RequestMapping值 */
	public static final String RETURL_SYN_ACTION = "/returnUrl";
	
	/** 预约授权后异步回调 @RequestMapping值 */
	public static final String RETURL_ASY_ACTION = "/notifyUrl";
	
}
