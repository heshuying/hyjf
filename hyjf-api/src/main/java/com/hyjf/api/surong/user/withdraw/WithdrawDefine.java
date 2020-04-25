package com.hyjf.api.surong.user.withdraw;

public class WithdrawDefine {
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
    public static final String REQUEST_MAPPING = "/surong/withdraw";
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



    /** JSP 汇付天下跳转画面 */
    public static final String JSP_CHINAPNR_RESULT = "/chinapnr/chinapnr_result";
    /** JSP 回调画面 */
    public static final String JSP_CHINAPNR_RECEIVE = "/chinapnr/chinapnr_receive";
    /** JSP 回调画面 */
    public static final String JSP_CHINAPNR_SEND = "/chinapnr/chinapnr_send";
}
