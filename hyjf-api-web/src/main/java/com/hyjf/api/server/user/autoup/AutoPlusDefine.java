package com.hyjf.api.server.user.autoup;

import com.hyjf.base.bean.BaseDefine;

public class AutoPlusDefine extends BaseDefine {
   
    
    public static final String PROJECT_NAME="/hyjf-api-web";
    
    /** 当前controller名称 */
    public static final String THIS_CLASS = AutoPlusServer.class.getName();
    
    
    //-------------------------------------------------------------------------------------------------
    public static final String CONTROLLER_NAME = "AutoPlusServer";
    /** @RequestMapping值 */
    public static final String REQUEST_MAPPING = "/server/autoPlus";
    /**
     * 请求地址 /server/trusteePay
     */
    public static final String REQUEST_URL_TRUSTEE_PAY = "/page";
    /**
     * 请求失败页面路径 /bank/user/trusteePay/error
     */
    public static final String PATH_TRUSTEE_PAY_ERROR = "/bank/user/trusteePay/error";
    /** 同步回调  */
    public static final String RETURL_SYN_ACTION = "/trusteePayReturn";
    /** 异步回调  */
    public static final String RETURL_ASY_ACTION = "/trusteePayBgreturn";
    
    
    
    /**标的状态7 */
    public static final Integer BORROW_STATUS_WITE_AUTHORIZATION = 7;
    
    /**查询接口*/
    public static final String RETURL_TRUSTEEPAYQUERY = "/trusteePayQuery";

    
    
    
    /**前导发送验证码*/
    public static final String SENDCODE_ACTION = "/sendcode";

    /** 用户自动出借增强授权 */
    public static final String USER_AUTH_INVES_ACTION = "/userAuthInves";
    
    /** 用户授权自动出借同步回调 */
    public static final String USER_AUTH_INVES_RETURN_ACTION = "/userAuthInvesReturn";
    /** 用户授权自动出借异步回调 */
    public static final String USER_AUTH_INVES_BGRETURN_ACTION = "/userAuthInvesBgreturn";
    
    /** 自动债权转让授权 */
    public static final String USER_CREDIT_AUTH_INVES_ACTION = "/userCreditAuthInves";
    /** 自动债权转让同步回调 */
    public static final String USER_CREDIT_AUTH_INVES_RETURN_ACTION = "/userCreditAuthInvesReturn";
    /** 自动债权转让异步回调 */
    public static final String USER_CREDIT_AUTH_INVES_BGRETURN_ACTION = "/userCreditAuthInvesBgreturn";

}
