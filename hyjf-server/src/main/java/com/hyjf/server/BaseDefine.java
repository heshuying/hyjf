package com.hyjf.server;

import com.hyjf.common.util.PropUtils;

public class BaseDefine {
    /** @RequestMapping值 */
    public static final String REQUEST_HOME = "/hyjf-server";
    
	/** 返回信息@RequestMapping值 */
	public static final String INFO = "INFO";
	/** 返回信息@RequestMapping值 */
	public static final String ERROR = "error";
	/** 返回结果@RequestMapping值 */
	public static final String STATUS = "status";
	/** 成功结果@RequestMapping值 */
	public static final Boolean STATUS_TRUE = true;
	/** 失败结果@RequestMapping值 */
	public static final Boolean STATUS_FALSE = false;
	/** hyjf.server.host */
	public static String HOST = PropUtils.getSystem("hyjf.server.host").trim();
    
    /** JSP 汇付天下跳转画面 */
    public static final String JSP_CHINAPNR_RESULT = "/chinapnr/chinapnr_result";
    /** JSP 回调画面 */
    public static final String JSP_CHINAPNR_RECEIVE = "/chinapnr/chinapnr_receive";
    /** JSP 回调画面 */
    public static final String JSP_CHINAPNR_SEND = "/chinapnr/chinapnr_send";
    
    /** JSP 失败页面 */
    public static final String JSP_ERROR = "/error/error";
}
