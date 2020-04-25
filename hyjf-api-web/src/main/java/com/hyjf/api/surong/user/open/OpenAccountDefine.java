/**
 * Description:用户开户常量类
 * Copyright: Copyright (HYJF Corporation)2015
 * Company: HYJF Corporation
 * @author: 王坤
 * @version: 1.0
 * Created at: 2015年12月4日 下午2:33:39
 * Modification History:
 * Modified by :
 */

package com.hyjf.api.surong.user.open;

import com.hyjf.base.bean.BaseDefine;


public class OpenAccountDefine extends BaseDefine {

	/** 用户开户 @RequestMapping值 */
	public static final String REQUEST_MAPPING = "/surong/openaccount";
	/** 开户 @RequestMapping值 */
	public static final String OPENACCOUNT_MAPPING = "/openAction";
	/** 开户汇付返回同步回调 @RequestMapping值 */
	public static final String RETURL_SYN_MAPPING = "/returl";
	/** 开户汇付返回异步回调 @RequestMapping值 */
	public static final String RETURN_ASY_MAPPING = "/bgreturl";
	
	/** JSP 汇付天下跳转画面 */
    public static final String JSP_CHINAPNR_SEND = "/chinapnr/chinapnr_send";
    /** JSP 回调画面 */
	public static final String OPEN_SUCCESS_PATH = "/open/open_success";
	/** JSP 回调画面 */
	public static final String OPEN_ERROR_PATH = "/open/open_error";
}
