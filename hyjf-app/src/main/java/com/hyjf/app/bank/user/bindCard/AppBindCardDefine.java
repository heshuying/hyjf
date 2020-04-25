package com.hyjf.app.bank.user.bindCard;

import com.hyjf.app.BaseDefine;

/**
 * App绑卡常量类
 * 
 * @author liuyang
 *
 */
public class AppBindCardDefine extends BaseDefine {


	/** @RequestMapping值 */
	public static final String REQUEST_BINDCARD = "/bindCard";


    /** 绑卡页面发送验证码 @RequestMapping 值 */
    public static final String REQUEST_SEND_SMS_CODE = "/user/bankCard/bind/smscode";

    /** 绑卡画面路径 @RequestMapping */
    public static final String BINDCARD_ACTION = "/user/bankCard/bind";
    /** 绑卡错误页面 */
    public static final String JUMP_HTML_ERROR_PATH = "/user/bankCard/bind/result/failed";
    /** 绑卡成功页面 */
    public static final String JUMP_HTML_SUCCESS_PATH = "/user/bankCard/bind/result/success";
    /** 绑卡处理中页面 */
    public static final String JUMP_HTML_HANDLING_PATH = "/user/bankCard/bind/result/handing";
	

}
