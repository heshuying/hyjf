package com.hyjf.api.server.user.trusteepay;

import com.hyjf.base.bean.BaseDefine;

public class TrusteePayDefine extends BaseDefine {
   
    
    public static final String PROJECT_NAME="/hyjf-api-web";
    
    /** 当前controller名称 */
    public static final String THIS_CLASS = TrusteePayServer.class.getName();
    
    
    //-------------------------------------------------------------------------------------------------
    public static final String CONTROLLER_NAME = "TrusteePayServer";
    /** @RequestMapping值 */
    public static final String REQUEST_MAPPING = "/server/trusteePay";
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
    
    

}
