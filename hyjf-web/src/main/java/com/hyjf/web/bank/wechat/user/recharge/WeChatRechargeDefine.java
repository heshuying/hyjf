package com.hyjf.web.bank.wechat.user.recharge;

import com.hyjf.web.BaseDefine;

/**
 * 微信充值常量类
 * 
 * @author liuyang
 *
 */
public class WeChatRechargeDefine extends BaseDefine {

	/** 微信充值 @RequestMapping值 */
	public static final String REQUEST_MAPPING = "/bank/wechat/user/recharge";

	/** 用户充值 @RequestMapping值 */
	public static final String RECHARGE_MAPPING = "/recharge";

	/** 获取用户银行卡信息 */
	public static final String SEARCHCARDINFO_MAPPING = "/searchCardInfo";

	/** 类名 */
	public static final String THIS_CLASS = WeChatRechargeController.class.getName();

	/** 充值同步回调地址 @RequestMapping值 */
	public static final String RETURN_MAPPING = "/return";

	/** 充值异步回调地址 @RequestMapping值 */
	public static final String CALLBACK_MAPPING = "/callback";

	/** 短信充值发送验证码 @RequestMapping值 */
	public static final String SENDCODE_ACTION = "/sendCode";

	/** 短信充值 @RequestMapping值 */
	public static final String RECHARGE_ONLINE_ACTION = "/rechargeOnline";

	/** 短信充值 @RequestMapping值 */
	public static final String RECHARGE_RESULT_ACTION = "/rechargeInfo";

}
