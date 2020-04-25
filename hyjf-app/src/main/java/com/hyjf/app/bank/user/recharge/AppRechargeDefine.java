package com.hyjf.app.bank.user.recharge;

import com.hyjf.app.BaseDefine;

public class AppRechargeDefine extends BaseDefine {
	/** 指定类型的项目 @RequestMapping值 */
	public static final String REQUEST_MAPPING = "/user/bank/recharge";
	/** @RequestMapping值 */
	public static final String GET_QP_RECHARGE_INFO_ACTION = "/getQpRechargeInfo";
	/** @RequestMapping值 */
	public static final String GET_RECHARGE_URL_ACTION = "/getRechargeUrl";
	/** @RequestMapping值 */
	public static final String USER_RECHARGE_ACTION = "/userRecharge";
	/** 获取充值信息 @RequestMapping值 */
	public static final String GET_QP_RECHARGE_INFO = "/hyjf-app/user/bank/recharge/getQpRechargeInfo";
	/** 充值URL @RequestMapping值 */
	public static final String GET_RECHARGE_URL = "/hyjf-app/user/bank/recharge/getRechargeUrl";
	/** 其他充值方式 URL @RequestMapping值 */
	public static final String RECHARGE_OTHER_URL = "/hyjf-app/user/bank/recharge/offLineRechageInfo";
	/** 充值规则URL @RequestMapping值 */
	public static final String RECHARGE_RULE_URL = "/user/bank/recharge/rechargeRule";
	/** @RequestMapping值 */
	public static final String RECHARGE_RULE_ACTION = "rechargeRule";
	/** 线下充值信息@RequestMapping值 */
	public static final String OFFLINERECHAGEINFO_ACTION = "offLineRechageInfo";

	public static final String OFFLINERECHAGEINFO__PATH = "/rechargeBottom";

	public static final String FEE = "充值手续费：";

	public static final String BALANCE = "实际到账：";

	public static final String RECHARGE_INFO_SUFFIX = "元；";

	/** JSP 回调画面 */
	public static final String JSP_CHINAPNR_SEND = "/chinapnr/chinapnr_send";

	public static final String RETURN_MAPPING = "/return";

	public static final String CALLBACK_MAPPING = "/callBack";

	/** 返回信息 */
	public static final String MESSAGE = "message";

	// 充值状态
	/** 失败 */
	public static final Integer STATUS_FAIL = 0;

	/** 成功 */
	public static final Integer STATUS_SUCCESS = 1;

	/** 充值中 */
	public static final Integer STATUS_RUNNING = 2;

	/** 充值异常 */
	public static final String RECHARGE_ERROR = "/recharge_fail";

	/** 充值成功 */
	public static final String RECHARGE_SUCCESS = "/recharge_success";

	/** 充值规则页面 */
	public static final String RECHARGE_RULE = "/recharge";

	/** 充值中 */
	public static final String RECHARGE_PROCESSING = "/recharge_processing";

	/** 充值成功页面 */
	public static final String RECHARGE_SUCCESS_HTML = "/user/bank/recharge/result/success";
	/** 充值处理中页面 */
	public static final String RECHARGE_HANDING_HTML = "/user/bank/recharge/result/handing";
	/** 充值失败页面 */
	public static final String RECHARGE_FAIL_HTML = "/user/bank/recharge/result/failed";

	/** 返回结果@RequestMapping值 */
	public static final String STATUS = "status";
	/** 成功结果@RequestMapping值 */
	public static final Boolean STATUS_TRUE = true;

	/** 短信充值发送验证码 @RequestMapping值 */
	public static final String SENDCODE_ACTION = "/sendCode";

	/** 短信充值 @RequestMapping值 */
	public static final String RECHARGE_ONLINE_ACTION = "/rechargeOnline";
	/** 短信充值给老老板用 @RequestMapping值 */
	public static final String RECHARGE_ONLINE_V1_ACTION = "/rechargeOnlinev1";
	/** 获取充值请求地址 @RequestMapping值 */
	public static final String SENDCODE_ACTION_FULL = "/hyjf-app/user/bank/recharge/sendCode";
	/** 获取充值请求地址 @RequestMapping值 */
	public static final String RECHARGE_ONLINE_ACTION_FULL = "/hyjf-app/user/bank/recharge/rechargeOnline";

	/** 短信充值 @RequestMapping值 */
	public static final String RECHARGE_RESULT_ACTION = "/rechargeInfo";
	
	/** 短信充值老路径 @RequestMapping */
	public static final String RECHARGE_ONLINE_INIT_PATH = "/bank/user/recharge/recharge";

}
