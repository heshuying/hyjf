package com.hyjf.wechat.controller.login;

import com.hyjf.wechat.base.BaseDefine;

/**
 * 
 * 登录 退出
 * @author sunss
 * @version hyjf 1.0
 * @since hyjf 1.0 2018年2月1日
 * @see 上午10:22:15
 */
public class LoginDefine extends BaseDefine {

    public static final String CONTROLLER_NAME="WXLoginController";
    
    public static final String SSO_CONTROLLER_NAME="SSOLoginController";
    
    /** 登录退出 @RequestMapping值 /wx/agreement */
    public static final String REQUEST_MAPPING = "/wx/login";
    /** 登录 @RequestMapping值 /doLogin */
    public static final String DOLOGIN_MAPPING = "/doLogin";
    /** 登出 @RequestMapping值 /doLoginOut */
    public static final String DOLOGINOUT_MAPPING = "/doLoginOut";
    /** 登录 @RequestMapping值 /doLogin */
    public static final String SSO_DOLOGIN_MAPPING = "/SSODoLogin";

}
