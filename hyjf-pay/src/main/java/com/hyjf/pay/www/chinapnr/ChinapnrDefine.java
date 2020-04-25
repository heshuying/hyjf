/**
 * Description:（类功能描述-必填） 需要在每个方法前添加业务描述，方便业务业务行为的BI工作
 * Copyright: Copyright (HYJF Corporation)2015
 * Company: HYJF Corporation
 * @author: Administrator
 * @version: 1.0
 * Created at: 2015年11月18日 下午12:34:08
 * Modification History:
 * Modified by :
 */

package com.hyjf.pay.www.chinapnr;

import com.hyjf.pay.lib.chinapnr.util.ChinaPnrConstant;

/**
 * @author Administrator
 */

public class ChinapnrDefine extends ChinaPnrConstant {

    /** CONTROLLOR @value值 */
    public static final String CONTROLLOR_CLASS_NAME = "ChinapnrController";

    /** CONTROLLOR @RequestMapping值 */
    public static final String CONTROLLOR_REQUEST_MAPPING = "/chinapnr";

    /** 画面 @RequestMapping值 */
    public static final String CALL_API = "callapi";
    /** 画面 @RequestMapping值 */
    public static final String CALL_API_BG = "callapibg";
    /** 画面 @RequestMapping值 */
    public static final String CALL_API_AJAX = "callapiajax";
    /** 画面 @RequestMapping值 */
    public static final String CALL_BACK = "callback";
    /** 画面 @RequestMapping值 */
    public static final String CALL_RETURN = "return";
    /** 企业用户绑定用户回调 @RequestMapping值 */
    public static final String BIND_CALL_RETURN = "bindReturn";
    /** 画面 @RequestMapping值 */
    public static final String GET_CHKVALUE = "getChkValue";

    /** chinapnrForm值 */
    public static final String CHINAPNR_FORM = "chinapnrForm";

    /** JSP 跳转到汇付天下画面 */
    public static final String JSP_CHINAPNR_SEND = "/chinapnr/chinapnr_send";
    /** JSP 汇付天下跳转画面 */
    public static final String JSP_CHINAPNR_RESULT = "/chinapnr/chinapnr_result";
    /** JSP 回调画面 */
    public static final String JSP_CHINAPNR_RECEIVE = "/chinapnr/chinapnr_receive";

}
