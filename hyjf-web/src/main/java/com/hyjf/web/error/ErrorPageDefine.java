package com.hyjf.web.error;

import com.hyjf.web.BaseDefine;

public class ErrorPageDefine extends BaseDefine {

    /** 异常页面控制器 @RequestMapping值 */
    public static final String REQUEST_MAPPING = "/error";
    
    /** 重复提交异常跳转 @RequestMapping值 */
    public static final String REPEAT_SUBMIT_ERROR_ACTION = "/repeatSubmitError";

    /** 重复提交异常页面 @RequestMapping值 */
    public static final String REPEAT_SUBMIT_ERROR_PAGE = "/error/repeat_submit_error";

}
