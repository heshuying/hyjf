package com.hyjf.api.web.referee;

import com.hyjf.base.bean.BaseDefine;

public class RefereeDefine extends BaseDefine {

    /** 权限 CONTROLLOR @RequestMapping值 */
    public static final String REQUEST_MAPPING = "/referee";

    /** 更新推荐人Action */
    public static final String UPDATE_REC_ACTION = "updateRec";

    /** 类名 */
    public static final String THIS_CLASS = RefereeServer.class.getName();
    
    /** ValidateForm请求返回值Key */
    public static final String REFERRER = "referrer";

    /** ValidateForm请求返回值Key */
    public static final String REFERRER_USERNAME = "referrerUserName";
    
    /** ValidateForm请求返回值Key */
    public static final String REFFER_MOBILE = "refferMobile";
    
    /** ValidateForm请求返回值Key */
    public static final String REFFER_ATTRIBUTE = "refferAttribute";
    
    /** ValidateForm请求返回值Key */
    public static final String JSON_VALID_STATUS_KEY = "status";

    /** ValidateForm请求返回值正常 */
    public static final String JSON_VALID_INFO_KEY = "info";

    /** ValidateForm请求返回值正常 */
    public static final String JSON_VALID_STATUS_OK = "y";

}
