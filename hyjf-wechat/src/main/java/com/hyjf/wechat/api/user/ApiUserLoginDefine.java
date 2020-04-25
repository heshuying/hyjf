package com.hyjf.wechat.api.user;

public class ApiUserLoginDefine {

	  /** @RequestMapping值 */
    public static final String REQUEST_MAPPING = "/api/user/login";
   
    /** 第三方登录 @RequestMapping值 */
    public static final String THIRD_LOGIN = "/third_login";
    
 // 结果页跳转处理页面
    public static final String JUMP_HTML = "/jumpHTML";
    // 跳转失败页面
    public static final String JUMP_FAILE_HTML = "/thirdparty/thirdAuthorResult/failed";
    // 跳转授权页面
    public static final String JUMP_BIND_HTML = "/thirdparty/login";
    // 授权成功跳转页面
    public static final String JUMP_BIND_SUCCESS_HTML = "/thirdparty/thirdAuthorResult/success";
}
