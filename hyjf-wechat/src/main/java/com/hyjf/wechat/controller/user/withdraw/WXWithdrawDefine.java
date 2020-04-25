package com.hyjf.wechat.controller.user.withdraw;

import com.hyjf.wechat.base.BaseDefine;

/**
 * 
 * 登录 退出
 * @author sunss
 * @version hyjf 1.0
 * @since hyjf 1.0 2018年2月1日
 * @see 上午10:22:15
 */
public class WXWithdrawDefine extends BaseDefine {

    public static final String CONTROLLER_NAME="WXWithdrawController";
    
    /** 提现 @RequestMapping值 */
    public static final String REQUEST_MAPPING = "/wx/withdraw";
    /** 获取提现信息  @RequestMapping值 */
    public static final String GETINFO_MAPPING = "/getInfo";
    /** @RequestMapping值 */
    public static final String CASH_MAPPING = "/cash";
    /** @RequestMapping值 */
    public static final String RETURN_MAPPING = "/return";
    /** @RequestMapping值 */
    public static final String CALLBACK_MAPPING = "/callback";
    /** @RequestMapping值 */
    public static final String GET_WITHDRAW_RULE_MAPPING = "/getRule";
	/** 提现规则  路径 */
	public static final String WITHDRAW_RULE_PATH = "/withdraw/withdraw";
	
    /**本地跳转的页面*/
    public static final String JUMP_HTML = "/user/result/jump_html";
    
    /** JSP 回调画面 */
    public static final String JSP_CHINAPNR_SEND = "/chinapnr/chinapnr_send";
    /** 提现成功结果页 */
    public static final String JUMP_HTML_SUCCESS_PATH = "/user/withdraw/result/success";
    /** 提现失败结果页 */
    public static final String JUMP_HTML_ERROR_PATH = "/user/withdraw/result/failed";
    /** 提取中间结果页 */
    public static final String JUMP_HTML_HANDLING_PATH = "/user/withdraw/result/handing";
	/** 提现成功页面  路径 */
	public static final String WITHDRAW_SUCCESS_PATH = "/withdraw/withdraw_cash_success";
	/** 提现处理中页面  路径 */
	public static final String WITHDRAW_INPROCESS_PATH = "/withdraw/withdraw_cash_inprocess";

}
