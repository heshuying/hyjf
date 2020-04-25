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

package com.hyjf.web.user.transfer;

import com.hyjf.web.BaseDefine;

public class TransferDefine extends BaseDefine {

	/** @RequestMapping值 */
	public static final String REQUEST_MAPPING = "/user/tansfer";
	
	/** 用户转账 @RequestMapping值 */
	public static final String INTI_TRANSFER_ACTION = "/init";
	
	/** 用户转账同步回调 @RequestMapping值 */
	public static final String RETURL_SYN_ACTION = "/returnUrl";
	
	/** 用户转账异步回调 @RequestMapping值 */
	public static final String RETURL_ASY_ACTION = "/bgreturnUrl";
	
	/** 转账成功@RequestMapping值 */
	public static final String TRANSFER_SUCCESS_PATH = "/user/transfer/transferSuccess";
	
	/** 转账失败@RequestMapping值 */
	public static final String TRANSFER_ERROR_PATH = "/user/transfer/transferError";
	
	/** 类名 */
	public static final String THIS_CLASS = TransferController.class.getName();

}
