/**
 * Description:出借常量类
 * Copyright: Copyright (HYJF Corporation)2015
 * Company: HYJF Corporation
 * @author: b
 * @version: 1.0
 *           Created at: 2015年12月4日 下午2:33:39
 *           Modification History:
 *           Modified by :
 */

package com.hyjf.web.bank.web.user.recharge;

import com.hyjf.web.BaseDefine;

public class RechargeDefine extends BaseDefine {

	/** 用户充值 @RequestMapping值 */
	public static final String REQUEST_MAPPING = "/bank/web/user/recharge";

	/** 充值画面 @RequestMapping值 */
	public static final String RECHARGEPAGE_MAPPING = "/rechargePage";

	/** 用户充值 @RequestMapping值 */
	public static final String RECHARGE_MAPPING = "/recharge";

	/** 充值同步回调地址 @RequestMapping值 */
	public static final String RETURN_MAPPING = "/return";

	/** 充值异步回调地址 @RequestMapping值 */
	public static final String CALLBACK_MAPPING = "/callback";

	/** JSP 回调画面 */
	public static final String JSP_RECHARGE_PAGE = "/bank/user/recharge/recharge";

	public static final String JSP_RECHARGE_NEW_PAGE = "/bank/user/recharge/recharge-new";


	/** 充值成功页面 */
	public static final String RECHARGE_SUCCESS = "/bank/user/recharge/recharge_success";

	/** 充值失败页面 */
	public static final String RECHARGE_ERROR = "/bank/user/recharge/recharge_error";

	/** 短信充值发送验证码 @RequestMapping值 */
	public static final String SENDCODE_ACTION = "/sendCode";

	/** 短信充值 @RequestMapping值 */
	public static final String RECHARGE_ONLINE_ACTION = "/rechargeOnline";

	/** 短信充值 @RequestMapping值 */
	public static final String RECHARGE_RESULT_ACTION = "/rechargeInfo";
	
	/** 快捷充值限额 @RequestMapping值 */
    public static final String RECHARGE_QUOTALIMIT_ACTION = "/rechargeQuotaLimit";
    
    public static final String RECHARGE_QUOTALIMIT_PATH = "/bank/user/recharge/recharge-limit";
	
}
