/**
 * Description:出借常量类
 * Copyright: Copyright (HYJF Corporation)2015
 * Company: HYJF Corporation
 * @author: b
 * @version: 1.0
 * Created at: 2015年12月4日 下午2:33:39
 * Modification History:
 * Modified by :
 */

package com.hyjf.web.user.bindcard;

import com.hyjf.web.BaseDefine;

public class UserBindCardDefine  extends BaseDefine{

	/** @RequestMapping值 */
	public static final String REQUEST_MAPPING = "/bindCard";
	/** @RequestMapping值 */
    public static final String CHECK_MAPPING = "/check";
	/** @RequestMapping值 */
    public static final String RETURN_MAPPING = "/return";
	  /** chinapnrForm值 */
    public static final String CHINAPNR_FORM = "chinapnrForm";
    /** JSP 汇付天下跳转画面 */
    public static final String JSP_CHINAPNR_RESULT = "/chinapnr/chinapnr_result";
    /** JSP 回调画面 */
    public static final String JSP_CHINAPNR_RECEIVE = "/chinapnr/chinapnr_receive";
    /** JSP 回调画面 */
    public static final String JSP_CHINAPNR_SEND = "/chinapnr/chinapnr_send";
    /** 绑卡错误页面 */
    public static final String JSP_BINDCARD_FALSE = "/user/bingcard/bindcard_false";
    /** 用户绑卡URL */
    public static final String CALLURL = "/hyjf-web/bindCard/bindCard";
}

