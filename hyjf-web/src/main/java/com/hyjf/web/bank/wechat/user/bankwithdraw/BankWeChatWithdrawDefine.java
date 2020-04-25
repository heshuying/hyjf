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

package com.hyjf.web.bank.wechat.user.bankwithdraw;

import com.hyjf.web.BaseDefine;

public class BankWeChatWithdrawDefine extends BaseDefine {

	/** @RequestMapping值 */
	public static final String REQUEST_MAPPING = "/bank/wechat/bankwithdraw";
	/** 跳转到提现页面 */
	public static final String TO_WITHDRAW = "/toWithdraw";
	/** 提现页面 */
	public static final String WITHDRAW = "/user/withdraw/withdraw";
	/** 提现页面返回成功 */
	public static final String WITHDRAW_SUCCESS = "/user/withdraw/withdraw_success";
	/** 提现页面 返回失败 */
	public static final String WITHDRAW_FALSE = "/user/withdraw/withdraw_false";
	/** 提现页面 提现前的信息提示 */
	public static final String WITHDRAW_INFO = "/user/withdraw/withdraw_info";
	/** 获取银行卡提现手续费 */
	public static final String GET_FEE = "/getFee";
	/** @RequestMapping值 */
	public static final String CHECK_MAPPING = "/check";
	/** 用户提现  @RequestMapping值 */
	public static final String CASH_MAPPING = "/cash";
	/**同步回调地址  @RequestMapping值 */
	public static final String RETURN_MAPPING = "/return";
	/** 异步回调地址 @RequestMapping值 */
	public static final String CALLBACK_MAPPING = "/callback";
}
