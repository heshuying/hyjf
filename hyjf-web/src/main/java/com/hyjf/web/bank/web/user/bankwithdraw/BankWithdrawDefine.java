/**
 * Description:提现常量类
 * Copyright: Copyright (HYJF Corporation)2015
 * Company: HYJF Corporation
 * @author: gogtz-T
 * @version: 1.0
 * Created at: 2015年12月4日 下午2:33:39
 * Modification History:
 * Modified by :
 */

package com.hyjf.web.bank.web.user.bankwithdraw;

import com.hyjf.web.BaseDefine;

public class BankWithdrawDefine extends BaseDefine {

	/** @RequestMapping值 */
	public static final String REQUEST_MAPPING = "/bank/web/withdraw";
	/** 跳转到提现页面 */
	public static final String TO_WITHDRAW = "/toWithdraw";
	/** 跳转到短信验证码确认页面 */
	public static final String TO_SMS_CONFIRM = "/toSMSConfirm";
	/** 提现页面 */
	public static final String WITHDRAW = "/bank/user/withdraw/withdraw";
	/** 提现页面返回成功 */
	public static final String WITHDRAW_SUCCESS = "/bank/user/withdraw/withdraw_success";
	/** 提现页面 返回失败 */
	public static final String WITHDRAW_FALSE = "/bank/user/withdraw/withdraw_false";
	/** 提现页面 提现前的信息提示 */
	public static final String WITHDRAW_INFO = "/bank/user/withdraw/withdraw_info";
	/** 提现页面 短信验证码确认页面 */
	public static final String WITHDRAW_CONFIRM = "/bank/user/withdraw/withdraw_confirm";
	/** 获取银行卡提现手续费 */
	public static final String GET_FEE = "/getFee";
	/** @RequestMapping值 */
	public static final String CHECK_MAPPING = "/check";
	/** @RequestMapping值 */
	public static final String CASH_MAPPING = "/cash";
	/** @RequestMapping值 */
	public static final String RETURN_MAPPING = "/return";
	/** @RequestMapping值 */
	public static final String CALLBACK_MAPPING = "/callback";
	/** @RequestMapping值 */
	public static final String WITHDRAWINFO_MAPPING = "/withdrawInfo";
}
