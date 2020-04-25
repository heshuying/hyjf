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

package com.hyjf.app.user.withdraw;

import com.hyjf.app.BaseDefine;

public class UserWithdrawDefine extends BaseDefine {
	
	public static final String HYJF_CHINAPNR_URL = "hyjf.chinapnr.url";
	

	/** 提现规则  路径 */
	public static final String WITHDRAW_RULE_PATH = "/withdraw/withdraw";
	/** 提现成功页面  路径 */
	public static final String WITHDRAW_SUCCESS_PATH = "/withdraw/withdraw_cash_success";
	/** 提现失败页面  路径 */
	public static final String WITHDRAW_ERROR_PATH = "/withdraw/withdraw_cash_fail";
	/** 提现处理中页面  路径 */
	public static final String WITHDRAW_INPROCESS_PATH = "/withdraw/withdraw_cash_inprocess";

    // ----------------------- @RequestMapping值 -------------------------------
    /** @RequestMapping值 */
    public static final String REQUEST_MAPPING = "/user/withdraw";
    /** 获取我的银行卡  */
    public static final String GET_BANKCARD_MAPPING = "/getBankCardAction";
    /** @RequestMapping值 */
    public static final String GET_WITHDRAW_RULE_MAPPING = "/getRule";
    /** @RequestMapping值 */
    public static final String GET_CASH_URL_MAPPING = "/getCashUrl";
    /** 获取提现信息  */
    public static final String GET_WITHDRAW_INFO_MAPPING = "/getInfoAction";
    /** @RequestMapping值 */
    public static final String CASH_CHECK_MAPPING = "/cashCheck";
    /** @RequestMapping值 */
    public static final String CASH_MAPPING = "/cash";
    /** @RequestMapping值 */
    public static final String RETURN_MAPPING = "/return";
    /** @RequestMapping值 */
    public static final String CALLBACK_MAPPING = "/callback";
    /** chinapnrForm值 */
    public static final String CHINAPNR_FORM = "chinapnrForm";


    // ----------------------- Request -------------------------------
    /** @RequestMapping值 */
    public static final String GET_BANKCARD_REQUEST = REQUEST_HOME + REQUEST_MAPPING + GET_BANKCARD_MAPPING;
    /** @RequestMapping值 */
    public static final String CASH_CHECK_REQUEST = REQUEST_HOME + REQUEST_MAPPING + CASH_CHECK_MAPPING;
    /** @RequestMapping值 */
    public static final String CASH_REQUEST = REQUEST_HOME + REQUEST_MAPPING + CASH_MAPPING;
    /** @RequestMapping值 */
    public static final String GET_CASH_URL_REQUEST = REQUEST_HOME + REQUEST_MAPPING + GET_CASH_URL_MAPPING;
    /** @RequestMapping值 */
    public static final String GET_WITHDRAW_INFO_REQUEST = REQUEST_HOME + REQUEST_MAPPING + GET_WITHDRAW_INFO_MAPPING;


    /** JSP 汇付天下跳转画面 */
    public static final String JSP_CHINAPNR_RESULT = "/chinapnr/chinapnr_result";
    /** JSP 回调画面 */
    public static final String JSP_CHINAPNR_RECEIVE = "/chinapnr/chinapnr_receive";
    /** JSP 回调画面 */
    public static final String JSP_CHINAPNR_SEND = "/chinapnr/chinapnr_send";

    /** 提现成功结果页 */
    public static final String JUMP_HTML_SUCCESS_PATH = "/user/withdraw/result/success";

    /** 提现失败结果页 */
    public static final String JUMP_HTML_ERROR_PATH = "/user/withdraw/result/failed";

    /** 提取中间结果页 */
    public static final String JUMP_HTML_HANDLING_PATH = "/user/withdraw/result/handing";
}
