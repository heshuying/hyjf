package com.hyjf.api.server.user.auth.repayauth;

import com.hyjf.base.bean.BaseDefine;

public class RepayAuthPlusDefine extends BaseDefine {
   
    
    public static final String PROJECT_NAME="/hyjf-api-web";
    
    /** 当前controller名称 */
    public static final String THIS_CLASS = RepayAuthPlusServer.class.getName();
    
    
    //-------------------------------------------------------------------------------------------------
    public static final String CONTROLLER_NAME = "RepayAuthPlusServer";
    /** @RequestMapping值 */
    public static final String REQUEST_MAPPING = "/server/user/repayAuthPlus";

    /**
     * 请求失败页面路径 /bank/user/trusteePay/error
     */
    public static final String PATH_TRUSTEE_PAY_ERROR = "/bank/user/trusteePay/error";
    /** 同步回调  */
    public static final String USER_REPAY_AUTH_ACTION = "/page";
    
    /** 同步回调  */
    public static final String RETURL_SYN_ACTION = "/repayAuthReturn";
    /** 异步回调  */
    public static final String RETURL_ASY_ACTION = "/repayAuthBgreturn";
    

}
