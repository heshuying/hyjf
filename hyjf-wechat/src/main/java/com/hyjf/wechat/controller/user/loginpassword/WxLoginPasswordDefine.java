/*
 * @Copyright: 2005-2018 www.hyjf.com. All rights reserved.
 */
package com.hyjf.wechat.controller.user.loginpassword;

import com.hyjf.wechat.base.BaseDefine;

/**
 * @author fuqiang
 * @version WxLoginPasswordDefine, v0.1 2018/3/23 10:40
 */
public class WxLoginPasswordDefine extends BaseDefine {

    /** 指定类型的项目 @RequestMapping值 */
    public static final String REQUEST_MAPPING = "/wx/user/resetpwd";
    /** 修改登录密码 */
    public static final String MODIFY_PASSWORD_ACTION = "/modify";
    /** 找回登录密码 */
    public static final String RESET_PASSWORD_ACTION = "/reset";
    /** 发送验证码 */
    public static final String SEND_VERIFICATIONCODE_ACTION = "/sendVerificationCode";
    /** 验证验证码 */
    public static final String VALIDATE_VERIFICATIONCODE_ACTION = "/validateVerificationCode";
    /** 重置登录密码 */
    public static final String RESET_PASSWORD = "/resetLoginPassword";

    /** 短信验证码状态,新验证码 */
    public static final Integer CKCODE_NEW = 0;
    /** 短信验证码状态,失效 */
    public static final Integer CKCODE_FAILED = 7;
    /** 短信验证码状态,已验 */
    public static final Integer CKCODE_YIYAN = 8;
    /** 短信验证码状态,已用 */
    public static final Integer CKCODE_USED = 9;

}
