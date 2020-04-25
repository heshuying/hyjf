package com.hyjf.api.server.user.tenderauth;

import com.hyjf.base.bean.BaseDefine;

public class TenderAuthDefine extends BaseDefine {
    public static final String CONTROLLER_NAME = "tenderAuthController";
    /** @RequestMapping值 */
    public static final String REQUEST_MAPPING = "/server/userTenderAuth";
    /** @RequestMapping值 */
    public static final String TENDER_AUTH = "/tenderAuth";
    /** 用户授权同步回调 */
    public static final String USER_AUTH_INVES_RETURN_ACTION = "/userAuthInvesReturn";
    /** 用户授权异步回调 */
    public static final String USER_AUTH_INVES_BGRETURN_ACTION = "/userAuthInvesBgreturn";
    
    public static final String PROJECT_NAME="/hyjf-api-web";
    
    public static final String AUTH_ERROR_PATH = "/bank/user/auth/error";
    
    public static final String CALL_BACK_TRANSPASSWORD_VIEW = "/callback/callback_transpassword";
    
    /** 当前controller名称 */
    public static final String THIS_CLASS = TenderAuthServer.class.getName();
    
    public static final String TXCODE_AUTH ="authModify";
    

}
