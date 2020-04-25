package com.hyjf.wechat.controller.user.recharge;

import com.hyjf.wechat.base.BaseDefine;

public class WxRechargeDefine extends BaseDefine {
	/** 指定类型的项目 @RequestMapping值 */
	public static final String REQUEST_MAPPING = "/wx/recharge";
	/** @RequestMapping值 */
	public static final String GET_QP_RECHARGE_INFO_ACTION = "/getQpRechargeInfo";
	
	/** 获取充值信息 @RequestMapping值 */
	public static final String GET_QP_RECHARGE_INFO = "/hyjf-wechat/wx/recharge/getQpRechargeInfo";
	
	/** 其他充值方式 URL @RequestMapping值 */
	public static final String RECHARGE_OTHER_URL = "/hyjf-wechat/wx/recharge/offLineRechageInfo";
	
	/** 充值规则URL @RequestMapping值 */
	public static final String RECHARGE_RULE_URL = "/wx/recharge/rechargeRule";

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
	
    /** 结果页跳转处理页面 */
    public static final String JUMP_HTML = "/user/result/jump_html";
    /** @RequestMapping值 */
	public static final String RECHARGE_RULE_ACTION = "/rechargeRule";

}
