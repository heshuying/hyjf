/**
 * Description:出借常量类
 * Copyright: Copyright (HYJF Corporation)2015
 * Company: HYJF Corporation
 * @author: b
 * @version: 1.0
 * Created at: 2015年12月4日 下午2:33:39
 * Modification History:
 * Modified by : 
 */
	
package com.hyjf.web.direct;

import com.hyjf.web.BaseDefine;

public class DriecTransDefine  extends BaseDefine{

	/** @RequestMapping值 */
	public static final String REQUEST_MAPPING = "/direct";
	/** 绑定用户 init*/
	public static final String BIND_USER_INIT = "/bindInit";
	/** 绑定用户 page*/
	public static final String BIND_USER_PAGE = "/direct/binduser";
	/** 绑定用户 */
	public static final String BIND_USER_MAPPING = "/bindUser";
	/** 修改绑定用户 */
	public static final String UPDATE_BIND_USER_MAPPING = "/updateBindUser";	
	/** 定向转账 init*/
	public static final String DIRECT_TRANS_INIT = "/directInit";
	/** 定向转账 page*/
	public static final String DIRECT_TRANS_PAGE = "/direct/directrans";
	/** 定向转账 */
	public static final String DIRECT_TRANS_MAPPING = "/direcTrans";
	/**check */
	public static final String CHECK_MAPPING = "/check";
	/**发送短信校验码*/
	public static final String SEND_CODE_MAPPING = "/sendSmsCode";
	
	/**页面展示form*/
	public static final String FORM = "directForm";
	/** 登陆页面*/
	public static final String LOGIN_PAGE = "user/login/login";
	/** 绑定成功页面*/
	public static final String BIND_SUCCESS_PAGE = "direct/bindsuccess";
	/** 绑定失败页面*/
	public static final String BIND_ERROR_PAGE = "direct/bindfail";
	/** 错误页面*/
	public static final String ERROR_PAGE = "error/systemerror";
	/**转账成功页面*/
	public static final String DIRECT_SUCCESS_PAGE = "direct/directsuccess";
	/** 转账失败页面*/
	public static final String DIRECT_ERROR_PAGE = "direct/directfail";
	
	/**汇付接口返回地址*/
    public static final String BIND_USER_RETURN_MAPPING = "/bindUserReturn.do";
	/**汇付接口返回地址*/
    public static final String DIRECT_TRANS_RETURN_MAPPING = "/direcTransReturn.do";

    /** JSP 汇付天下跳转画面 */
    public static final String JSP_CHINAPNR_RESULT = "/chinapnr/chinapnr_result";
    /** JSP 回调画面 */
    public static final String JSP_CHINAPNR_RECEIVE = "/chinapnr/chinapnr_receive";
    /** JSP 回调画面 */
    public static final String JSP_CHINAPNR_SEND = "/chinapnr/chinapnr_send";
    

}

	