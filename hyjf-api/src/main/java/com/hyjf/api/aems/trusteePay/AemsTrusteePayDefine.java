package com.hyjf.api.aems.trusteePay;

import com.hyjf.base.bean.BaseDefine;

public class AemsTrusteePayDefine extends BaseDefine {

    /** @RequestMapping值 */
    public static final String REQUEST_MAPPING = "/aems/trusteePay";
    /**
     * 借款人受托支付申请请求地址 /server/trusteePay
     */
    public static final String TRUSTEE_PAY_ACTION = "/pay";
    /**
     * 请求失败页面路径 /bank/user/trusteePay/error
     */
    public static final String PATH_TRUSTEE_PAY_ERROR = "/bank/user/trusteePay/error";
    /** 同步回调  */
    public static final String SYN_ACTION = "/trusteePayReturn";
    /** 异步回调  */
    public static final String ASY_ACTION = "/trusteePayBgReturn";
    
    
    
    /**标的状态7 */
    public static final Integer BORROW_STATUS_WITE_AUTHORIZATION = 7;
    
    /**查询接口*/
    public static final String TRUSTEEPAYQUERY_ACTION = "/trusteePayQuery";
    
    

}
