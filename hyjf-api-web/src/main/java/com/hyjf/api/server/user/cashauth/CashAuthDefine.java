package com.hyjf.api.server.user.cashauth;

import com.hyjf.base.bean.BaseDefine;

public class CashAuthDefine extends BaseDefine {
    public static final String CONTROLLER_NAME = "cashAuthController";
    /** @RequestMapping值 */
    public static final String REQUEST_MAPPING = "/server/userCashauth";
    /** @RequestMapping值 */
    public static final String CASH_WITHDRAWAL = "/cashWithdrawal";
    /** 用户授权同步回调 */
    public static final String USER_AUTH_INVES_RETURN_ACTION = "/userAuthInvesReturn";
    /** 用户授权异步回调 */
    public static final String USER_AUTH_INVES_BGRETURN_ACTION = "/userAuthInvesBgreturn";
    
    public static final String PROJECT_NAME="/hyjf-api-web";
    
    public static final String AUTH_ERROR_PATH = "/bank/user/auth/error";
    
    public static final String CALL_BACK_TRANSPASSWORD_VIEW = "/callback/callback_transpassword";
    
    /** 当前controller名称 */
    public static final String THIS_CLASS = CashAuthorizationServer.class.getName();
    
    public static final String TXCODE_AUTH ="authModify";
    

}
