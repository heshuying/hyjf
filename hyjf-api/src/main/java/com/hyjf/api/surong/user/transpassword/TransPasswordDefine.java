package com.hyjf.api.surong.user.transpassword;

public class TransPasswordDefine {
    /** @RequestMapping值 */
    public static final String REQUEST_MAPPING = "/surong/user/transpassword";
    /** @RequestMapping值 */
    public static final String SETPASSWORD_ACTION = "/setPassword";
    
    /** 设置密码失败页面*/
    public static final String PASSWORD_ERROR_PATH = "/bank/user/personalsetting/error";
    /** @RequestMapping值 */
    public static final String RETURL_SYN_PASSWORD_ACTION = "/passwordReturn";
    /** @RequestMapping值 */
    public static final String RETURN_ASY_PASSWORD_ACTION = "/passwordBgreturn";
    /** 设置密码成功页面*/
    public static final String PASSWORD_SUCCESS_PATH = "/bank/user/personalsetting/success";
    /** 重置交易密码 */
    public static final String RESETPASSWORD_ACTION = "/resetPassword";
    /** 修改交易密码同步回调 */
    public static final String RETURL_SYN_RESETPASSWORD_ACTION = "/resetPasswordReturn";
    /** 修改交易密码异步回调 */
    public static final String RETURN_ASY_RESETPASSWORD_ACTION = "/resetPasswordBgreturn";
}
