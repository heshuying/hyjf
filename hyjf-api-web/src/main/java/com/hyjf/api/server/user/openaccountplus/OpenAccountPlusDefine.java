package com.hyjf.api.server.user.openaccountplus;

import com.hyjf.base.bean.BaseDefine;

/**
 * Created by yaoyong on 2017/11/29.
 */
public class OpenAccountPlusDefine extends BaseDefine{
    /** @RequestMapping值*/
    public static final String REQUEST_MAPPING = "/server/user/plus/openaccountplus";
    /** 开户发送短信验证码*/
    public static final String SEND_SMS_ACTION = "/sendsmscode";
    /** 用户开户 @RequestMapping */
    public static final String OPEN_ACCOUNT_ACTION = "/open";
    /** 注册 */
     public static final String METHOD_SERVER_REGISTER ="/register";
    /** 类名*/
    public static final String THIS_CLASS = OpenAccountPlusServer.class.getName();
}
